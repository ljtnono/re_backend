package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorHardDiskVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorIOVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import cn.lingjiatong.re.service.sys.service.BackendSystemMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台系统监控模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 14:20
 */
@RestController
public class BackendSystemMonitorController implements BackendSystemMonitorFeignClient {

    @Autowired
    private BackendSystemMonitorService backendSystemMonitorService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/cpuInfo")
    @PassToken
    public ResultVO<BackendSystemMonitorCPUVO> findCPUInfo() {
        return ResultVO.success(backendSystemMonitorService.findCPUInfo());
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/memoryInfo")
    @PassToken
    public ResultVO<BackendSystemMonitorMemoryVO> findMemoryInfo() {
        return ResultVO.success(backendSystemMonitorService.findMemoryInfo());
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/hardDiskInfo")
    @PassToken
    public ResultVO<List<BackendSystemMonitorHardDiskVO>> findHardDiskInfo() {
        return ResultVO.success(backendSystemMonitorService.findHardDiskInfo());
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/ioInfo")
    @PassToken
    public ResultVO<BackendSystemMonitorIOVO> findIOInfo() {
        return ResultVO.success(backendSystemMonitorService.findIOInfo());
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
