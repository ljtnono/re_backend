package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
     *
     * @param categoryId 文章分类id
     * @return 存在返回true， 不存在返回false
     */
    public boolean isExistById(Integer categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        return category != null;
    }

}
