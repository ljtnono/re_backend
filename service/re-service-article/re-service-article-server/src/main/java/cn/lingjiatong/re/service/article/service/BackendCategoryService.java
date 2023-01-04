package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
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
