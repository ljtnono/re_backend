package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 前端消息feign接口层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/11 00:28
 */
@FeignClient(name = "re-service-sys-server", path = "/sys", contextId = "FrontendNoticeFeignClient")
public interface FrontendNoticeFeignClient {

    /**
     * 获取前端通知信息列表
     *
     * @return 前端通知列表VO对象列表
     */
    @GetMapping("/api/v1/noticeList")
    @ApiOperation(value = "获取前端通知信息列表", httpMethod = "GET")
    ResultVO<List<FrontendNoticeListVO>> findFrontendNoticeList();

}
