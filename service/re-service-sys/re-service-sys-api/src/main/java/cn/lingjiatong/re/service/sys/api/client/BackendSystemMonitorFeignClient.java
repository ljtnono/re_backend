package cn.lingjiatong.re.service.sys.api.client;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.config.FeignBasicAuthRequestInterceptor;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 获取k8s集群名称空间列表
     *
     * @param currentUser 当前登陆用户
     * @return 后台系统监控获取k8s集群名称空间列表VO对象列表
     */
    @GetMapping("/backend/api/v1/systemMonitor/namespaceList")
    ResultVO<List<BackendSystemMonitorNamespaceListVO>> findNamespaceList(@SpringQueryMap User currentUser);

    /**
     * 获取系统硬盘信息
     *
     * @param ipAddr 主机ip地址
     * @param currentUser 当前登录用户
     * @return 后台系统监控硬盘信息VO对象列表
     */
    @GetMapping("/backend/api/v1/systemMonitor/hardDiskInfo")
    ResultVO<List<BackendSystemMonitorHardDiskVO>> findHardDiskInfo(@RequestParam("ipAddr") String ipAddr, @SpringQueryMap User currentUser);

    /**
     * 获取k8s集群节点列表
     *
     * @param currentUser 当前登录用户
     * @return 后台系统监控k8s节点列表
     */
    @GetMapping("/backend/api/v1/systemMonitor/k8sNodeList")
    ResultVO<List<BackendSystemMonitorK8sNodeListVO>> findK8sNodeList(@SpringQueryMap User currentUser);

    /**
     * 获取主机cpu信息
     *
     * @param ipAddr 主机ip地址
     * @param currentUser 当前登录用户
     * @return 后台系统监控cpu VO对象
     */
    @GetMapping("/backend/api/v1/systemMonitor/cpuInfo")
    ResultVO<BackendSystemMonitorCPUVO> findCPUInfo(@RequestParam("ipAddr") String ipAddr, @SpringQueryMap User currentUser);

    /**
     * 获取主机内存信息
     *
     * @param ipAddr 主机ip地址
     * @param currentUser 当前登录用户
     * @return 后台系统监控内存 VO对象
     */
    @GetMapping("/backend/api/v1/systemMonitor/memoryInfo")
    ResultVO<BackendSystemMonitorMemoryVO> findMemoryInfo(@RequestParam("ipAddr") String ipAddr, @SpringQueryMap User currentUser);

    /**
     * 获取k8s集群pod列表
     *
     * @param namespace k8s集群名称空间
     * @param currentUser 当前登录用户
     * @return 后台系统监控k8s集群pod列表VO对象列表
     */
    @GetMapping("/backend/api/v1/systemMonitor/k8sPodList")
    ResultVO<List<BackendSystemMonitorPodListVO>> findK8sPodList(@RequestParam("namespace") String namespace, @SpringQueryMap User currentUser);

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
