package cn.lingjiatong.re.api.backend.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.annotation.CurrentUser;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.client.BackendSystemMonitorFeignClient;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorK8sNodeListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorNamespaceListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 获取k8s节点列表
     *
     * @param currentUser 当前登录用户
     * @return 后台系统监控获取k8s节点列表VO对象列表对象
     */
    @GetMapping("/k8sNodeList")
    @Operation(summary = "获取k8s节点列表", method = "GET")
    @PreAuthorize("hasAnyAuthority('system:monitor')")
    public ResultVO<List<BackendSystemMonitorK8sNodeListVO>> findK8sNodeList(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========获取k8s节点列表");
        return backendSystemMonitorFeignClient.findK8sNodeList(currentUser);
    }

    /**
     * 获取k8s名称空间列表
     *
     * @param currentUser 当前登录用户
     * @return 后台系统监控k8s名称空间列表VO对象列表
     */
    @GetMapping("/k8sNamespaceList")
    @Operation(summary = "获取k8s名称空间列表", method = "GET")
    @PreAuthorize("hasAnyAuthority('system:monitor')")
    public ResultVO<List<BackendSystemMonitorNamespaceListVO>> findK8sNamespaceList(@Parameter(hidden = true) @CurrentUser User currentUser) {
        log.info("==========获取k8s名称空间列表");
        return backendSystemMonitorFeignClient.findNamespaceList(currentUser);
    }

    // ********************************私有函数********************************
    // ********************************公用函数********************************
}
