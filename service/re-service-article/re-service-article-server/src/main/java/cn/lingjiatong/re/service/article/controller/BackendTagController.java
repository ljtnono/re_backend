package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.article.api.client.BackendTagFeignClient;
import cn.lingjiatong.re.service.article.api.vo.BackendTagListVO;
import cn.lingjiatong.re.service.article.entity.Tag;
import cn.lingjiatong.re.service.article.service.BackendTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后端文章标签模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 20:10
 */
@RestController
public class BackendTagController implements BackendTagFeignClient {

    @Autowired
    private BackendTagService backendTagService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/tag/list")
    public ResultVO<List<BackendTagListVO>> findBackendTagList(User currentUser) {
        return ResultVO.success(backendTagService.findTagList(Tag::getId, Tag::getName));
    }

}
