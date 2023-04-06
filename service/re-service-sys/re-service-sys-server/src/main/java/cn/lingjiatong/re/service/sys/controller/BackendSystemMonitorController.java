package cn.lingjiatong.re.service.sys.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.PassToken;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.*;
import cn.lingjiatong.re.service.sys.properties.KubernetesProperties;
import cn.lingjiatong.re.service.sys.service.BackendSystemMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/backend/api/v1/systemMonitor/namespaceList")
    public ResultVO<List<BackendSystemMonitorNamespaceListVO>> findNamespaceList(User currentUser) {
        return ResultVO.success(backendSystemMonitorService.findNamespaceList());
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/hardDiskInfo")
    @PassToken
    public ResultVO<List<BackendSystemMonitorHardDiskVO>> findHardDiskInfo(@RequestParam("ipAddr") String ipAddr, User currentUser) {
        ipAddr = ipAddr.split(",")[0];
        KubernetesProperties.KubernetesNode node = backendSystemMonitorService.findK8sNodeInfoByIpAddr(ipAddr);
        if (node == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        return ResultVO.success(backendSystemMonitorService.findHardDiskInfo(ipAddr, node.getSshPort(), node.getSshUsername(), node.getSshPassword()));
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/k8sNodeList")
    public ResultVO<List<BackendSystemMonitorK8sNodeListVO>> findK8sNodeList(User currentUser) {
        return ResultVO.success(backendSystemMonitorService.findK8sNodeList());
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/cpuInfo")
    @PassToken
    public ResultVO<BackendSystemMonitorCPUVO> findCPUInfo(@RequestParam("ipAddr") String ipAddr, User currentUser) {
        ipAddr = ipAddr.split(",")[0];
        KubernetesProperties.KubernetesNode node = backendSystemMonitorService.findK8sNodeInfoByIpAddr(ipAddr);
        if (node == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        return ResultVO.success(backendSystemMonitorService.findCPUInfo(ipAddr, node.getSshPort(), node.getSshUsername(), node.getSshPassword()));
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/memoryInfo")
    @PassToken
    public ResultVO<BackendSystemMonitorMemoryVO> findMemoryInfo(@RequestParam("ipAddr") String ipAddr, User currentUser) {
        ipAddr = ipAddr.split(",")[0];
        KubernetesProperties.KubernetesNode node = backendSystemMonitorService.findK8sNodeInfoByIpAddr(ipAddr);
        if (node == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        return ResultVO.success(backendSystemMonitorService.findMemoryInfo(ipAddr, node.getSshPort(), node.getSshUsername(), node.getSshPassword()));
    }

    @Override
    @GetMapping("/backend/api/v1/systemMonitor/k8sPodList")
    @PassToken
    public ResultVO<List<BackendSystemMonitorPodListVO>> findK8sPodList(@RequestParam("namespace") String namespace, User currentUser) {
        namespace = namespace.split(",")[0];
        return ResultVO.success(backendSystemMonitorService.findK8sPodList(namespace));
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
