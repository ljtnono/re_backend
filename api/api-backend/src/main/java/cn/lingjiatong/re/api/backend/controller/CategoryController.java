package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.BackendCategoryFeignClient;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategorySaveDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端管理系统Category模块接口
 *
 * @author Ling, Jiatong
 * Date: 2023/1/4 16:55
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Tag(name = "后端管理系统Category模块接口")
public class CategoryController {

    @Autowired
    private BackendCategoryFeignClient backendCategoryFeignClient;

    // ********************************新增类接口********************************

    /**
     * 后端新增文章分类
     *
     * @param dto 后台新增文章分类DTO对象
     * @return 通用消息返回对象
     */
    @PostMapping("/save")
    @Operation(summary = "后端新增文章分类", method = "POST")
    public ResultVO<?> saveCategory(@RequestBody BackendCategorySaveDTO dto) {
        log.info("==========后端新增文章分类，参数：{}", dto);
        return backendCategoryFeignClient.saveCategory(dto);
    }

    // ********************************删除类接口********************************

    /**
     * 后端批量删除文章分类
     *
     * @param dto 后台批量删除文章分类DTO对象
     * @return 通用消息返回对象
     */
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "后端批量删除文章分类", method = "DELETE")
    public ResultVO<?> deleteCategoryBatch(@RequestBody BackendCategoryDeleteBatchDTO dto) {
        log.info("==========后端批量删除文章分类，参数：{}", dto);
        return backendCategoryFeignClient.deleteCategoryBatch(dto);
    }

    // ********************************修改类接口********************************

    /**
     * 后端修改文章分类
     *
     * @param dto 后台修改文章分类DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/update")
    @Operation(summary = "后端修改文章分类", method = "PUT")
    public ResultVO<?> updateCategory(@RequestBody BackendCategoryUpdateDTO dto) {
        log.info("==========后端修改文章分类，参数：{}", dto);
        return backendCategoryFeignClient.updateCategory(dto);
    }

    // ********************************查询类接口********************************

    /**
     * 后端获取文章分类列表
     *
     * @return 后端获取文章分类列表VO对象列表
     */
    @GetMapping("/list")
    @Operation(summary = "后端获取文章分类列表", method = "GET")
    public ResultVO<List<BackendCategoryListVO>> findCategoryList() {
        log.info("==========后端获取文章分类列表");
        return backendCategoryFeignClient.findCategoryList();
    }
}
