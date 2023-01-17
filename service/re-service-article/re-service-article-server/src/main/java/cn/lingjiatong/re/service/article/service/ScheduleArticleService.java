package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.DistributedTaskStatusEnum;
import cn.lingjiatong.re.common.constant.RedissionLockKeyEnum;
import cn.lingjiatong.re.common.entity.es.ESArticle;
import cn.lingjiatong.re.service.article.bo.ArticleTagListBO;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 定时任务文章模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 20:23
 */
@Slf4j
@Service
public class ScheduleArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RedissonClient redissonClient;


    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 同步文章列表到ES
     *
     * @see cn.lingjiatong.re.common.constant.DistributedTaskStatusEnum
     * @return 执行状态
     */
    public Integer syncArticleToES() {
        // 先获取需要同步的文章列表
        RLock lock = redissonClient.getLock(RedissionLockKeyEnum.ES_ARTICLE_SYNC_LOCK.getValue());
        boolean isLock;
        try {
            // 2000ms拿不到锁，就认为获取锁失败。5000ms是锁失效时间.
            isLock = lock.tryLock(2000, 5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // 获取锁失败
            return DistributedTaskStatusEnum.LOCK_FAILED.getCode();
        } finally {
            // 释放锁
            lock.unlock();
        }

        // 获取锁成功
        if (isLock) {
            try {
                List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .orderByDesc(Article::getCreateTime));
                if (CollectionUtils.isEmpty(articleList)) {
                    lock.unlock();
                    return DistributedTaskStatusEnum.FINISHED.getCode();
                }

                // 获取文章的标签列表
                Map<Long, List<String>> articleIdTagNameListMap = Maps.newHashMap();
                List<Long> articleIdList = articleList.stream().map(Article::getId).collect(Collectors.toList());
                List<ArticleTagListBO> boList = tagMapper.findTagNameListByArticleIdList(articleIdList);
                boList.forEach(bo -> {
                    Long articleId = bo.getArticleId();
                    String tagNameStr = bo.getTagNameStr();
                    if (!StringUtils.hasLength(tagNameStr)) {
                        articleIdTagNameListMap.put(articleId, List.of());
                    } else {
                        articleIdTagNameListMap.put(articleId, Arrays.stream(tagNameStr.split(",")).collect(Collectors.toList()));
                    }
                });

                log.info("==========开始同步es数据，共{}条", articleList.size());
                // 同步数据
                articleList.forEach(article -> {
                    ESArticle esArticle = new ESArticle();
                    BeanUtils.copyProperties(article, esArticle);
                    esArticle.setTagList(articleIdTagNameListMap.get(article.getId()));
                    ESArticle exist = elasticsearchRestTemplate.get(String.valueOf(article.getId()), ESArticle.class);
                    if (exist != null) {
                        // 先删除后保存
                        elasticsearchRestTemplate.delete(String.valueOf(article.getId()), ESArticle.class);
                    }
                    elasticsearchRestTemplate.save(esArticle);
                });
                log.error("==========结束同步es数据");
                return DistributedTaskStatusEnum.FINISHED.getCode();
            } catch (Exception e) {
                log.error(e.toString(), e);
                return DistributedTaskStatusEnum.ERROR_FINISHED.getCode();
            } finally {
                lock.unlock();
            }
        } else {
            // 获取锁失败
            return DistributedTaskStatusEnum.LOCK_FAILED.getCode();
        }
    }
}
