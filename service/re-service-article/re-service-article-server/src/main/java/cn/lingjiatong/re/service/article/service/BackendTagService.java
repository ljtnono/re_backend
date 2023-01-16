package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.article.api.vo.BackendTagListVO;
import cn.lingjiatong.re.service.article.bo.ArticleTagListBO;
import cn.lingjiatong.re.service.article.entity.Tag;
import cn.lingjiatong.re.service.article.entity.TrArticleTag;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import cn.lingjiatong.re.service.article.mapper.TrArticleTagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后端文章标签模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 20:31
 */
@Slf4j
@Service
public class BackendTagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private TrArticleTagMapper trArticleTagMapper;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    // ********************************私有函数********************************
    // ********************************公共函数********************************

    /**
     * 批量删除文章标签对应关系
     *
     * @param articleIdList 文章id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTrArticleTagBatch(List<Long> articleIdList) {
        if (CollectionUtils.isEmpty(articleIdList)) {
            return;
        }
        trArticleTagMapper.delete(new LambdaQueryWrapper<TrArticleTag>()
                .in(TrArticleTag::getArticleId, articleIdList));
    }

    /**
     * 根据文章id列表获取文章标签列表
     *
     * @param articleIdList 文章id列表
     * @return 文章对应的标签列表
     */
    public Map<Long, List<String>> findTagListByArticleIdList(List<Long> articleIdList) {
        Map<Long, List<String>> resultMap = Maps.newHashMap();
        List<ArticleTagListBO> boList = tagMapper.findTagNameListByArticleIdList(articleIdList);
        if (CollectionUtils.isEmpty(boList)) {
            return resultMap;
        }
        boList.forEach(bo -> {
            Long articleId = bo.getArticleId();
            String tagNameStr = bo.getTagNameStr();
            if (!StringUtils.hasLength(tagNameStr)) {
                resultMap.put(articleId, List.of());
            } else {
                resultMap.put(articleId, Arrays.stream(tagNameStr.split(",")).collect(Collectors.toList()));
            }
        });
        return resultMap;
    }

    /**
     * 批量保存标签
     * 此函数不校验标签名是否正确，调用前应该进行校验
     *
     * @param tagList 标签名列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Long articleId, List<String> tagList) {
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        List<Tag> existTagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getName)
                .in(Tag::getName, tagList));
        if (!CollectionUtils.isEmpty(existTagList)) {
            List<String> tagNameList = existTagList.stream().map(Tag::getName).collect(Collectors.toList());
            // 移除已经存在的
            tagList = tagList.stream().filter(t -> !tagNameList.contains(t)).collect(Collectors.toList());
        }
        // 只插入不存在的
        tagList.forEach(tag -> {
            Tag t = new Tag();
            t.setId(snowflakeIdWorkerUtil.nextId());
            t.setCreateTime(DateUtil.getLocalDateTimeNow());
            t.setModifyTime(DateUtil.getLocalDateTimeNow());
            t.setOptUser(UserConstant.SUPER_ADMIN_USER);
            t.setName(tag);
            t.setDeleted(CommonConstant.ENTITY_NORMAL);
            try {
                // TODO 此处进行优化SQL，改为只执行一个SQL
                tagMapper.insert(t);
                // 插入关联表
                TrArticleTag trArticleTag = new TrArticleTag();
                trArticleTag.setId(snowflakeIdWorkerUtil.nextId());
                trArticleTag.setTagId(t.getId());
                trArticleTag.setArticleId(articleId);
                trArticleTagMapper.insert(trArticleTag);
            } catch (DuplicateKeyException e) {
                log.warn("==========重复标签");
            }
        });
    }

    /**
     * 后端获取文章标签列表
     *
     * @param fields 需要获取的字段列表
     * @return 后端获取文章标签列表VO对象列表
     */
    public List<BackendTagListVO> findTagList(SFunction<Tag, ?>... fields) {
        List<Tag> tagList;
        if (fields == null || fields.length == 0) {
            tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getDeleted, CommonConstant.ENTITY_NORMAL));
        } else {
            tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                    .select(fields)
                    .eq(Tag::getDeleted, CommonConstant.ENTITY_NORMAL));
        }
        return tagList.stream().map(tag -> {
            BackendTagListVO vo = new BackendTagListVO();
            BeanUtils.copyProperties(tag, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
