package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.client.FrontendTagFeignClient;
import cn.lingjiatong.re.service.article.api.vo.FrontendTagListVO;
import cn.lingjiatong.re.service.article.service.FrontendTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端博客标签模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:27
 */
@RestController
public class FrontendTagController implements FrontendTagFeignClient {

    @Autowired
    private FrontendTagService frontendTagService;

    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************


    @Override
    @GetMapping("/frontend/api/v1/tag/hotTagList")
    public ResultVO<List<FrontendTagListVO>> findFrontendHotTagList() {
        return ResultVO.success(frontendTagService.findFrontendHotTagList());
    }
}
