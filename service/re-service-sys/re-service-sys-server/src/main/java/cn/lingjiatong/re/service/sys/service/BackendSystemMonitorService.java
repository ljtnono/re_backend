package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorCPUVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorK8sNodeListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendSystemMonitorMemoryVO;
import cn.lingjiatong.re.service.sys.util.KubernetesUtil;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeAddress;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1NodeStatus;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * 后台系统监控service层
 *
 * @author Ling, Jiatong
 * Date: 4/3/23 8:53 PM
 */
@Service
public class BackendSystemMonitorService {


    private final static String HOST = "www.lingjiatong.cn";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "ljtLJT715336";
    // 主机ip地址与ssh的端口映射关系
    private final static Map<String, Integer> HOST_IP_SSH_PORT_MAP = Map.of(
            "192.168.", 8003,
            "", 8004,
            "", 8005,
            "", 8006
    );


    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    /**
     * 获取主机内存信息
     *
     * @param hostIPAddr 主机ip地址
     * @return 后台系统监控内存VO对象
     */
    public BackendSystemMonitorMemoryVO findHostMemoryInfo(String hostIPAddr) throws JSchException, IOException {
        BackendSystemMonitorMemoryVO vo = new BackendSystemMonitorMemoryVO();
        String command = "free -m";
        JSch jsch = new JSch();
        Session session = jsch.getSession(USERNAME, HOST, HOST_IP_SSH_PORT_MAP.get(hostIPAddr));
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));

        // 读取输出
        char[] buffer = new char[1024];
        bufferedReader.read(buffer);
        String stringBuilder = new String(buffer);
        channelExec.disconnect();
        session.disconnect();


        String memoryInfo = stringBuilder.toString();
        String[] lines = memoryInfo.split("\n");
        String[] memoryData = lines[1].split("\\s+");

        double totalMemory = Double.parseDouble(memoryData[1]) / 1024;
        double usedMemory = Double.parseDouble(memoryData[2]) / 1024;
        double freeMemory = Double.parseDouble(memoryData[3]) / 1024;
        double memoryUsage = usedMemory / totalMemory;

        vo.setTotalMemory(totalMemory + "GB");
        vo.setUsedMemory(usedMemory + "GB");
        vo.setFreeMemory(freeMemory + "GB");
        vo.setMemoryUsedPercent(memoryUsage * 100 + "%");
        return vo;
    }


    /**
     * 获取主机CPU信息
     *
     * @param hostIPAddr 主机ip地址
     * @return 后台系统监控CPUVO对象
     */
    public BackendSystemMonitorCPUVO findHostCPUInfo(String hostIPAddr) throws JSchException, IOException {
        // 创建 JSch 对象
        JSch jsch = new JSch();
        // 创建会话
        Session session = jsch.getSession(USERNAME, HOST, HOST_IP_SSH_PORT_MAP.get(hostIPAddr));
        // 设置密码
        session.setPassword(PASSWORD);
        // 配置属性
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        // 连接服务器
        session.connect();
        // 创建执行命令的通道
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        // 获取 CPU 核心数
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        CentralProcessor processor = hal.getProcessor();
        int cpuCount = processor.getLogicalProcessorCount();
        channel.setCommand("top -b -n1 | grep 'Cpu(s)'");
        // 连接通道
        channel.connect();
        // 读取输出
        byte[] buffer = new byte[1024];
        channel.getInputStream().read(buffer);
        String output = new String(buffer);
        // 关闭通道
        channel.disconnect();
        // 解析输出
        String[] fields = output.trim().split("\\s+");
        // 用户使用率
        double userPercent = Double.parseDouble(fields[1]);
        double systemPercent = Double.parseDouble(fields[3]);
        double idlePercent = Double.parseDouble(fields[7]);
        // 计算总 CPU 使用率
//        double totalPercent = (userPercent + systemPercent) / cpuCount;

        BackendSystemMonitorCPUVO vo = new BackendSystemMonitorCPUVO();
        vo.setCpuCoreNum(cpuCount);
        vo.setUserUsedPercent(String.valueOf(userPercent * 100));
        vo.setSystemUsedPercent(String.valueOf(systemPercent * 100));
        vo.setFreePercent(String.valueOf(idlePercent * 100));
        return vo;
    }


    /**
     * 获取k8s节点列表
     *
     * @return 后台获取k8s节点列表VO对象列表
     */
    public List<BackendSystemMonitorK8sNodeListVO> findK8sNodeList() throws ApiException {
        KubernetesUtil k8sUtil = KubernetesUtil.getInstance();
        k8sUtil.initClient();
        V1NodeList nodeList = k8sUtil.getNodeList();
        List<V1Node> itemList = nodeList.getItems();
        List<BackendSystemMonitorK8sNodeListVO> result = Lists.newArrayList();
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
        return result;
    }

}
