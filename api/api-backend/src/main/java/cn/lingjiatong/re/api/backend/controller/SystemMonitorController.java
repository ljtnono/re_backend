package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorHardDiskVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorIOVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统监控模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2023/4/6 10:22
 */
@Slf4j
@RestController
@RequestMapping("/systemMonitor")
@Tag(name = "后台系统监控模块接口")
public class SystemMonitorController {

    @Autowired
    private BackendSystemMonitorFeignClient backendSystemMonitorFeignClient;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取本机cpu信息
     *
     * @return 后台系统监控cpu VO对象
     */
    @GetMapping("/cpuInfo")
    @Operation(summary = "获取本机cpu信息", method = "GET")
    public ResultVO<BackendSystemMonitorCPUVO> findCPUInfo() {
        log.info("==========获取本机cpu信息");
        return backendSystemMonitorFeignClient.findCPUInfo();
    }

    /**
     * 获取本机内存信息
     *
     * @return 后台系统监控内存 VO对象
     */
    @GetMapping("/memoryInfo")
    @Operation(summary = "获取本机内存信息", method = "GET")
    public ResultVO<BackendSystemMonitorMemoryVO> findMemoryInfo() {
        log.info("==========获取本机内存信息");
        return backendSystemMonitorFeignClient.findMemoryInfo();
    }

    /**
     * 获取本机硬盘信息
     *
     * @return 后台系统监控硬盘信息VO对象列表
     */
    @GetMapping("/hardDiskInfo")
    @Operation(summary = "获取本机硬盘信息", method = "GET")
    public ResultVO<List<BackendSystemMonitorHardDiskVO>> findHardDiskInfo() {
        log.info("==========获取本机硬盘信息");
        return backendSystemMonitorFeignClient.findHardDiskInfo();
    }

    /**
     * 获取本机磁盘、网络IO速率
     *
     * @return 后台系统监控磁盘网络IO VO对象
     */
    @GetMapping("/ioInfo")
    @Operation(summary = "获取本机磁盘、网络IO速率", method = "GET")
    public ResultVO<BackendSystemMonitorIOVO> findIOInfo() {
        log.info("==========获取本机磁盘、网络IO速率");
        return backendSystemMonitorFeignClient.findIOInfo();
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
