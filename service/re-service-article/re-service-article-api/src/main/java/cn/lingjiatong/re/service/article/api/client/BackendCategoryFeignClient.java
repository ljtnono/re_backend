package cn.lingjiatong.re.service.article.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryDeleteBatchDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategorySaveDTO;
import cn.lingjiatong.re.service.article.api.dto.BackendCategoryUpdateDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendCategoryListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 后台文章分类模块feign客户端接口层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:12
 */
@FeignClient(name = "re-service-article-server", path = "/article", contextId = "BackendCategoryFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendCategoryFeignClient {

    // ********************************新增类接口********************************

    /**
     * 后端新增文章分类
     *
     * @param dto 后台新增文章分类DTO对象
     * @return 通用消息返回对象
     */
    @PostMapping("/backend/api/v1/category/save")
    ResultVO<?> saveCategory(@RequestBody BackendCategorySaveDTO dto);

    // ********************************删除类接口********************************

    /**
     * 后端批量删除文章分类
     *
     * @param dto 后台批量删除文章分类DTO对象
     * @return 通用消息返回对象
     */
    @DeleteMapping("/backend/api/v1/category/deleteBatch")
    ResultVO<?> deleteCategoryBatch(@RequestBody BackendCategoryDeleteBatchDTO dto);

    // ********************************修改类接口********************************

    /**
     * 后端修改文章分类
     *
     * @param dto 后台修改文章分类DTO对象
     * @return 通用消息返回对象
     */
    @PutMapping("/backend/api/v1/category/update")
    ResultVO<?> updateCategory(@RequestBody BackendCategoryUpdateDTO dto);

    // ********************************查询类接口********************************

    /**
     * 后端获取文章分类列表
     *
     * @return 后端获取文章分类列表VO对象列表
     */
    @GetMapping("/backend/api/v1/category/list")
    ResultVO<List<BackendCategoryListVO>> findCategoryList();

}
