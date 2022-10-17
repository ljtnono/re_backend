package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.RedisCacheKeyEnum;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.common.util.RandomUtil;
import cn.lingjiatong.re.common.util.RedisUtil;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleSaveDTO;
import cn.lingjiatong.re.service.article.constant.BackendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * 后台文章模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:58
 */
@Slf4j
@Service
public class BackendArticleServcie {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private RedisUtil redisUtil;

    // ********************************新增类接口********************************

    /**
     * 后端保存文章接口
     *
     * @param dto 后台保存文章接口DTO对象
     */
    public void saveArticle(BackendArticleSaveDTO dto) {

    }


    /**
     * 保存后端文章封面图片
     *
     * @param multipartFile 图片
     * @return 图片的minio访问地址
     */
    public String saveArticleCoverImage(MultipartFile multipartFile) {

        return null;
    }


    /**
     * 后端保存文章草稿
     * 默认保存到redis中
     *
     * @param markdownContent 文章草稿markdown内容
     */
    public void saveDraftArticle(String draftTitle, String markdownContent) {
        if (StringUtils.isEmpty(draftTitle)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), BackendArticleErrorMessageConstant.DRAFT_TITLE_EMPTY_ERROR_MESSAGE);
        }
        Optional.ofNullable(markdownContent)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));

        // 先获取上次已保存的值，如果不存在则设置，存在则删除再设置
        Set<String> keys = redisUtil.keys("daft:" + draftTitle + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(key -> {
                redisUtil.deleteObject(key);
            });
        }

        String redisKey = RedisCacheKeyEnum.ARTICLE_DRAFT.getValue()
                .replace("draftTitle", draftTitle)
                .replace("timestamp", String.valueOf(System.currentTimeMillis()))
                .replace("uuid", RandomUtil.getInstance().generateSimpleUUID());

        // 不设置过期时间
        redisUtil.setCacheObject(redisKey, markdownContent);
    }

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************


}
