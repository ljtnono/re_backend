package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.SaUserUtils;
import cn.lingjiatong.re.service.article.api.client.BackendCategoryFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategorySaveDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import cn.lingjiatong.re.service.article.service.BackendCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台文章分类模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:25
 */
@RestController
public class BackendCategoryController implements BackendCategoryFeignClient {

    @Autowired
    private BackendCategoryService backendCategoryService;

    // ********************************新增类接口********************************

    @Override
    @PostMapping("/backend/api/v1/category/save")
    public ResultVO<?> saveCategory(BackendCategorySaveDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendCategoryService.saveCategory(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************删除类接口********************************

    @Override
    @DeleteMapping("/backend/api/v1/category/deleteBatch")
    public ResultVO<?> deleteCategoryBatch(BackendCategoryDeleteBatchDTO dto) {
        backendCategoryService.deleteCategoryBatch(dto);
        return ResultVO.success();
    }

    // ********************************修改类接口********************************

    @Override
    @PutMapping("/backend/api/v1/category/update")
    public ResultVO<?> updateCategory(BackendCategoryUpdateDTO dto) {
        User currentUser = SaUserUtils.getCurrentUser();
        backendCategoryService.updateCategory(dto, currentUser);
        return ResultVO.success();
    }

    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/category/list")
    public ResultVO<List<BackendCategoryListVO>> findCategoryList() {
        return ResultVO.success(backendCategoryService.findCategoryList());
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************

}
