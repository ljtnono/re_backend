package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorHardDiskVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorIOVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 后台系统监控模块feign客户端接口
 *
 * @author Ling, Jiatong
 * Date: 2023/4/4 14:26
 */
@FeignClient(value = "re-service-sys-server", path = "/sys", contextId = "BackendSystemMonitorFeignClient", configuration = {FeignBasicAuthRequestInterceptor.class})
public interface BackendSystemMonitorFeignClient {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取本机cpu信息
     *
     * @return 后台系统监控cpu VO对象
     */
    @GetMapping("/backend/api/v1/systemMonitor/cpuInfo")
    ResultVO<BackendSystemMonitorCPUVO> findCPUInfo();

    /**
     * 获取本机内存信息
     *
     * @return 后台系统监控内存 VO对象
     */
    @GetMapping("/backend/api/v1/systemMonitor/memoryInfo")
    ResultVO<BackendSystemMonitorMemoryVO> findMemoryInfo();

    /**
     * 获取本机硬盘信息
     *
     * @return 后台系统监控硬盘信息VO对象列表
     */
    @GetMapping("/backend/api/v1/systemMonitor/hardDiskInfo")
    ResultVO<List<BackendSystemMonitorHardDiskVO>> findHardDiskInfo();

    /**
     * 获取本机磁盘、网络IO速率
     *
     * @return 后台系统监控磁盘网络IO VO对象
     */
    @GetMapping("/backend/api/v1/systemMonitor/ioInfo")
    ResultVO<BackendSystemMonitorIOVO> findIOInfo();

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
