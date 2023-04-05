package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.service.sys.api.vo.*;
import cn.lingjiatong.re.service.sys.properties.KubernetesProperties;
import cn.lingjiatong.re.service.sys.util.KubernetesUtil;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台系统监控service层
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 8:53 PM
 */
@Slf4j
@Service
public class BackendSystemMonitorService {

    @Autowired
    private KubernetesProperties kubernetesProperties;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取k8s集群名称空间列表
     *
     * @return 后台系统监控k8s集群名称空间列表VO对象列表
     */
    public List<BackendSystemMonitorNamespaceListVO> findNamespaceList() {
        List<BackendSystemMonitorNamespaceListVO> result = Lists.newArrayList();
        try {
            V1NamespaceList namespaceList = KubernetesUtil.getInstance().getNamespaceList();
            List<V1Namespace> n = namespaceList.getItems();
            if (!CollectionUtils.isEmpty(n)) {
                for (V1Namespace v1Namespace : n) {
                    String name = v1Namespace.getMetadata().getName();
                    BackendSystemMonitorNamespaceListVO vo = new BackendSystemMonitorNamespaceListVO();
                    vo.setName(name);
                    result.add(vo);
                }
            }
        } catch (ApiException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }
        return result;
    }

    /**
     * 获取系统监控硬盘信息
     *
     * @param ipAddr 主机ip地址
     * @param port 主机ssh端口号
     * @param username ssh用户名
     * @param password ssh密码
     * @return 后台系统监控硬盘VO对象列表
     */
    public List<BackendSystemMonitorHardDiskVO> findHardDiskInfo(String ipAddr, int port, String username, String password) {
        if (!StringUtils.hasLength(ipAddr)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 创建 JSch 对象
        JSch jsch = new JSch();
        Session session;
        ChannelExec channel;
        try {
            // 创建session并链接服务器
            session = jsch.getSession(username, ipAddr, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect();
        } catch (JSchException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        String command = "df -h";
        List<BackendSystemMonitorHardDiskVO> result = Lists.newArrayList();
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();
            // 读取输出
            InputStream in = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("Filesystem")) {
                    String[] fields = line.split("\\s+");
                    String filesystem = fields[0];
                    String totalSize = fields[1];
                    String usedSize = fields[2];
                    String availableSize = fields[3];
                    String usedPercent = fields[4];
                    String mountPoint = fields[5];

                    BackendSystemMonitorHardDiskVO vo = new BackendSystemMonitorHardDiskVO();
                    vo.setMountPoint(mountPoint);
                    vo.setFileSystem(filesystem);
                    vo.setTotalSize(totalSize);
                    vo.setAvailableSize(availableSize);
                    vo.setUsedSize(usedSize);
                    vo.setUsedPercent(usedPercent);
                    result.add(vo);
                }
            }
            // 关闭通道
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        return result;
    }

    /**
     * 获取k8s节点列表
     *
     * @return 后台获取k8s节点列表VO对象列表
     */
    public List<BackendSystemMonitorK8sNodeListVO> findK8sNodeList() {
        KubernetesUtil k8sUtil = KubernetesUtil.getInstance();
        List<BackendSystemMonitorK8sNodeListVO> result = Lists.newArrayList();
        try {
            V1NodeList nodeList = k8sUtil.getNodeList();
            List<V1Node> itemList = nodeList.getItems();
            itemList.forEach(item -> {
                BackendSystemMonitorK8sNodeListVO vo = new BackendSystemMonitorK8sNodeListVO();
                V1NodeStatus status = item.getStatus();
                List<V1NodeAddress> addresses = status.getAddresses();
                addresses.forEach(address -> {
                    String type = address.getType();
                    String addr = address.getAddress();
                    if ("InternalIP".equals(type)) {
                        vo.setNodeIPAddr(addr);
                    } else if ("Hostname".equals(type)) {
                        vo.setNodeHostName(addr);
                    }
                });
                result.add(vo);
            });
        } catch (ApiException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }
        return result;
    }

    /**
     * 获取主机CPU信息
     *
     * @param ipAddr 主机ip地址
     * @param port 主机ssh端口号
     * @param username ssh用户名
     * @param password ssh密码
     * @return 后台系统监控CPU VO对象
     */
    public BackendSystemMonitorCPUVO findCPUInfo(String ipAddr, int port, String username, String password) {
        if (!StringUtils.hasLength(ipAddr)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }
        // 创建 JSch 对象
        JSch jsch = new JSch();
        Session session;
        ChannelExec channel;
        try {
            // 创建session并链接服务器
            session = jsch.getSession(username, ipAddr, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect();
        } catch (JSchException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        String cpuCountCommand = "grep -c processor /proc/cpuinfo";
        String cpuUsedInfoCommand = "top -b -n1 | grep 'Cpu(s)'";
        StringBuilder cpuInfoResultStringBuilder = new StringBuilder();
        StringBuilder cpuCountResultStringBuilder = new StringBuilder();
        try {
            // 创建执行命令的通道
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cpuUsedInfoCommand);
            channel.connect();
            // 读取输出
            byte[] buffer = new byte[1024];
            InputStream inputStream = channel.getInputStream();
            while (inputStream.read(buffer, 0, buffer.length) != -1) {
                cpuInfoResultStringBuilder.append(new String(buffer, StandardCharsets.UTF_8));
            }
            // 关闭通道
            channel.disconnect();

            // 创建执行命令的通道
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cpuCountCommand);
            channel.connect();
            InputStream in = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                cpuCountResultStringBuilder.append(line);
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        String cpuUsedInfo = cpuInfoResultStringBuilder.toString().trim();
        String cpuCountInfo = cpuCountResultStringBuilder.toString().trim();
        if (!StringUtils.hasLength(cpuUsedInfo) || !cpuUsedInfo.startsWith("%Cpu(s)")) {
            log.error("cpu占用信息解析异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }
        if (!StringUtils.hasLength(cpuCountInfo)) {
            log.error("cpu核心数解析异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        // 解析输出
        String[] fields = cpuUsedInfo.split("\\s+");
        double userUsedPercent = BigDecimal.valueOf(Double.parseDouble(fields[1]))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        double systemUsedPercent = BigDecimal.valueOf(Double.parseDouble(fields[3]))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        double idlePercent = BigDecimal.valueOf(Double.parseDouble(fields[7]))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        int cpuCount = Integer.valueOf(cpuCountInfo);

        BackendSystemMonitorCPUVO vo = new BackendSystemMonitorCPUVO();
        vo.setCpuCoreNum(cpuCount);
        vo.setUserUsedPercent(userUsedPercent + "%");
        vo.setSystemUsedPercent(systemUsedPercent + "%");
        vo.setFreePercent(idlePercent + "%");
        return vo;
    }

    /**
     * 获取主机内存信息
     *
     * @param ipAddr 主机ip地址
     * @param port 主机ssh端口号
     * @param username ssh用户名
     * @param password ssh密码
     * @return 后台系统监控内存VO对象
     */
    public BackendSystemMonitorMemoryVO findMemoryInfo(String ipAddr, int port, String username, String password) {
        if (!StringUtils.hasLength(ipAddr)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }

        JSch jsch = new JSch();
        Session session;
        ChannelExec channel;
        try {
            session = jsch.getSession(username, ipAddr, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect();
        } catch (JSchException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        String command = "free";
        StringBuilder memoryInfoResultStringBuilder = new StringBuilder();
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();
            // 读取输出
            byte[] buffer = new byte[1024];
            InputStream inputStream = channel.getInputStream();
            while (inputStream.read(buffer, 0, buffer.length) != -1) {
                memoryInfoResultStringBuilder.append(new String(buffer, StandardCharsets.UTF_8));
            }
            // 关闭通道
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | IOException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        String memoryInfo = memoryInfoResultStringBuilder.toString().trim();
        if (!StringUtils.hasLength(memoryInfo)) {
            log.error("内存占用信息解析异常");
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        BackendSystemMonitorMemoryVO vo = new BackendSystemMonitorMemoryVO();
        String[] lines = memoryInfo.split("\n");
        String[] memoryData = lines[1].split("\\s+");
        double totalMemory = BigDecimal.valueOf(Double.parseDouble(memoryData[1]))
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .doubleValue();
        double usedMemory = BigDecimal.valueOf(Double.parseDouble(memoryData[2]))
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .doubleValue();
        double availableMemory = BigDecimal.valueOf(Double.parseDouble(memoryData[6]))
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(1024), RoundingMode.HALF_UP)
                .doubleValue();
        double memoryUsage = BigDecimal.valueOf(usedMemory)
                .setScale(2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(totalMemory), RoundingMode.HALF_UP)
                .doubleValue();

        vo.setTotalMemory(totalMemory + "GB");
        vo.setUsedMemory(usedMemory + "GB");
        vo.setAvailableMemory(availableMemory + "GB");
        vo.setMemoryUsedPercent(memoryUsage * 100 + "%");
        return vo;
    }

    /**
     * 获取k8s集群的pod列表
     *
     * @param namespace 名称空间
     * @return 后台系统监控获取k8s集群的pod列表VO对象列表
     */
    public List<BackendSystemMonitorPodListVO> findK8sPodList(String namespace) {
        if (!StringUtils.hasLength(namespace)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR);
        }

        V1PodList v1PodList;
        try {
            v1PodList = KubernetesUtil.getInstance().getPodList(namespace);
        } catch (ApiException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.SYSTEM_MONITOR_ERROR);
        }

        List<BackendSystemMonitorPodListVO> result = Lists.newArrayList();
        List<V1Pod> items = v1PodList.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return result;
        }

        items.forEach(item -> {
            String name = "<none>";
            String ready = "<none>";
            String status = "<none>";
            String restarts = "<none>";
            String age = "<none>";
            String ip = "<none>";
            String node = "<none>";
            String nominatedNode = "<none>";
            String readinessGates = "<none>";

            // name
            V1ObjectMeta metadata = item.getMetadata();
            if (metadata != null) {
                name = metadata.getName();
            }

            V1PodStatus v1PodStatus = item.getStatus();
            if (v1PodStatus != null) {
                // ready
                List<V1ContainerStatus> containerStatusList = v1PodStatus.getContainerStatuses();
                if (!CollectionUtils.isEmpty(containerStatusList)) {
                    int readyCount = 0;
                    int totalCount = containerStatusList.size();
                    for (V1ContainerStatus containerStatus : containerStatusList) {
                        if (containerStatus.getReady()) {
                            readyCount++;
                        }
                    }
                    ready = readyCount + "/" + totalCount;
                }
                // status
                status = item.getStatus().getPhase();

                OffsetDateTime lastRestartTime = v1PodStatus.getStartTime();
                if (lastRestartTime != null) {
                    // restarts
                    if (!CollectionUtils.isEmpty(containerStatusList)) {
                        V1ContainerStatus firstContainerStatus = v1PodStatus.getContainerStatuses().get(0);
                        Duration lastRestartTimeduration = Duration.between(lastRestartTime, OffsetDateTime.now());
                        StringBuilder restartStringBuilder = new StringBuilder(String.valueOf(firstContainerStatus.getRestartCount()));
                        if (lastRestartTimeduration.toMinutes() < 60) {
                            restartStringBuilder.append(" (").append(lastRestartTimeduration.toMinutes()).append("m ago)");
                        } else if (lastRestartTimeduration.toHours() < 24) {
                            restartStringBuilder.append(" (").append(lastRestartTimeduration.toHours()).append("h ago)");
                        } else {
                            restartStringBuilder.append(" (").append(lastRestartTimeduration.toDays()).append("d ago)");
                        }
                        restarts = restartStringBuilder.toString();
                    }

                    // age
                    Duration duration = Duration.between(lastRestartTime, OffsetDateTime.now(ZoneOffset.UTC));
                    long totalSeconds = duration.getSeconds();
                    long days = totalSeconds / (60 * 60 * 24);
                    long hours = (totalSeconds % (60 * 60 * 24)) / (60 * 60);
                    age = days + "d" + hours + "h";
                }

                // ip
                ip = v1PodStatus.getPodIP();

                // nominatedNodeName
                if (StringUtils.hasLength(v1PodStatus.getNominatedNodeName())) {
                    nominatedNode = v1PodStatus.getNominatedNodeName();
                }
            }

            V1PodSpec spec = item.getSpec();
            if (spec != null) {
                // node
                node = spec.getNodeName();

                // readinessGates
                List<V1PodReadinessGate> readinessGateList = spec.getReadinessGates();
                if (!CollectionUtils.isEmpty(readinessGateList)) {
                    readinessGates = readinessGateList
                            .stream()
                            .map(V1PodReadinessGate::getConditionType)
                            .collect(Collectors.joining(","));
                }
            }

            BackendSystemMonitorPodListVO vo = new BackendSystemMonitorPodListVO();
            vo.setName(name);
            vo.setReady(ready);
            vo.setStatus(status);
            vo.setRestarts(restarts);
            vo.setAge(age);
            vo.setIp(ip);
            vo.setNode(node);
            vo.setNominatedNode(nominatedNode);
            vo.setReadinessGates(readinessGates);
            result.add(vo);
        });

        return result;
    }

    // ********************************私有函数********************************

    // ********************************公共函数********************************

    /**
     * 根据主机的ip地址获取主机相关信息
     *
     * @param ipAddr 主机ip地址
     * @return 主机信息map
     */
    public KubernetesProperties.KubernetesNode findK8sNodeInfoByIpAddr(String ipAddr) {
        List<KubernetesProperties.KubernetesNode> nodeList = kubernetesProperties.getNodeList();
        for (KubernetesProperties.KubernetesNode node : nodeList) {
            String ip = node.getIpAddr();
            if (ip.equalsIgnoreCase(ipAddr)) {
                return node;
            }
        }
        return null;
    }

}
