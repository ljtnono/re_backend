package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategorySaveDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import cn.lingjiatong.re.service.article.entity.Article;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.ArticleMapper;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 后端文章分类模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/19 20:45
 */
@Slf4j
@Service
public class BackendCategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;

    // ********************************新增类接口********************************

    /**
     * 后端新增文章分类
     *
     * @param dto         后台新增文章分类DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(BackendCategorySaveDTO dto, User currentUser) {
        String name = dto.getName();
        if (!StringUtils.hasLength(name)) {
            throw new BusinessException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 分类名校验
        Long nameExistCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, name)
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
        if (nameExistCount > 0) {
            throw new BusinessException(ErrorEnum.CATEGORY_NAME_EXIST_ERROR);
        }
        Category category = new Category();
        category.setId(snowflakeIdWorkerUtil.nextId());
        category.setName(name);
        category.setView(0L);
        category.setFavorite(0L);
        category.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        category.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        category.setDeleted(CommonConstant.ENTITY_NORMAL);
        category.setOptUser(currentUser.getUsername());
        categoryMapper.insert(category);
    }

    // ********************************删除类接口********************************

    /**
     * 后端批量删除文章分类（软删除）
     * <p>
     * 分类下存在未删除文章时禁止删除
     *
     * @param dto 后台批量删除文章分类DTO对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategoryBatch(BackendCategoryDeleteBatchDTO dto) {
        Set<Long> categoryIdSet = dto.getCategoryIdSet();
        if (CollectionUtils.isEmpty(categoryIdSet)) {
            return;
        }
        // 校验分类下是否存在未删除文章
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdSet)
                .eq(Article::getDeleted, CommonConstant.ENTITY_NORMAL));
        if (articleCount > 0) {
            throw new BusinessException(ErrorEnum.CATEGORY_HAS_ARTICLE_ERROR);
        }
        categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                .set(Category::getDeleted, CommonConstant.ENTITY_DELETE)
                .set(Category::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .in(Category::getId, categoryIdSet)
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
    }

    // ********************************修改类接口********************************

    /**
     * 后端修改文章分类
     *
     * @param dto         后台修改文章分类DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(BackendCategoryUpdateDTO dto, User currentUser) {
        Long id = dto.getId();
        String name = dto.getName();
        if (id == null || !StringUtils.hasLength(name)) {
            throw new BusinessException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 分类名校验（排除自身）
        Long nameExistCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, name)
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL)
                .ne(Category::getId, id));
        if (nameExistCount > 0) {
            throw new BusinessException(ErrorEnum.CATEGORY_NAME_EXIST_ERROR);
        }
        categoryMapper.update(null, new LambdaUpdateWrapper<Category>()
                .set(Category::getName, name)
                .set(Category::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .set(Category::getOptUser, currentUser.getUsername())
                .eq(Category::getId, id)
                .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
    }

    // ********************************查询类接口********************************

    /**
     * 根据分类id校验文章分类是否存在
     * 这里不论是否删除
     *
     * @param categoryId 文章分类id
     * @return 存在返回true， 不存在返回false
     */
    public boolean isExistById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        return category != null;
    }

    /**
     * 后端获取文章分类列表
     *
     * @param fields 需要获取的字段列表
     * @return 后端获取文章分类列表VO对象列表
     */
    public List<BackendCategoryListVO> findCategoryList(SFunction<Category, ?>... fields) {
        List<Category> categoryList;
        if (fields == null || fields.length == 0) {
            categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
        } else {
            categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .select(fields)
                    .eq(Category::getDeleted, CommonConstant.ENTITY_NORMAL));
        }
        return categoryList.stream().map(category -> {
            BackendCategoryListVO vo = new BackendCategoryListVO();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).collect(Collectors.toList());
    }

}
