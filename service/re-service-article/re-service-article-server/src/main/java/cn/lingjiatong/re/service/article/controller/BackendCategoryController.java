package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.BackendCategoryFeignClient;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import cn.lingjiatong.re.service.article.entity.Category;
import cn.lingjiatong.re.service.article.service.BackendCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端文章分类模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:10
 */
@RestController
public class BackendCategoryController implements BackendCategoryFeignClient {

    @Autowired
    private BackendCategoryService backendCategoryService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/category/list")
    public ResultVO<List<BackendCategoryListVO>> findCategoryList() {
        return ResultVO.success(backendCategoryService.findCategoryList(Category::getId, Category::getName));
    }
}
