package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleVO;
import cn.lingjiatong.re.service.article.constant.FrontendArticleErrorMessageConstant;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 前端article模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 21:39
 */
@Slf4j
@Service
public class FrontendArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;

    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取单个文章信息
     *
     * @param articleId 文章id
     * @return 单个文章信息VO对象
     */
    @NonNull
    public FrontendArticleVO findArticle(@NonNull Long articleId) {
        if (articleId < 0) {
            throw new BusinessException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), FrontendArticleErrorMessageConstant.ARTICLE_NOT_EXIST);
        }
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .eq(Article::getId, articleId)
                .eq(Article::getDeleted, CommonConstant.ENTITY_NORMAL));
        Optional.ofNullable(article)
                .orElseThrow(() -> new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR));

        // 获取其标签列表和分类名
        FrontendArticleVO result = new FrontendArticleVO();
        BeanUtils.copyProperties(article, result);
        result.setId(String.valueOf(articleId));
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getName)
                .eq(Category::getId, article.getCategoryId())
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
        result.setCategory(category.getName());
        // TODO 这里先写死，后面修改为作者名
        result.setAuthor(UserConstant.SUPER_ADMIN_USER);
        // 获取标签列表
        List<String> tagList = tagMapper.findTagListByArticleId(articleId);
        result.setTagList(tagList);
        return result;
    }
}
