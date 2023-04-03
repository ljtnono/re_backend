package cn.lingjiatong.re.service.sys.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;



/**
 * @author Ling, Jiatong
 * Date: 2023/4/3 16:56
 */
public class RemoteCpuMonitor {

    public static void main(String[] args) throws Exception {
        String host = "www.lingjiatong.cn";
        String user = "root";
        String password = "ljtLJT715336";
        int interval = 5; // 监控间隔，单位秒

        try {
            // 创建 JSch 对象
            JSch jsch = new JSch();

            // 创建会话
            Session session = jsch.getSession(user, host, 8007);

            // 设置密码
            session.setPassword(password);

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

            while (true) {
                // 执行命令
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
                double userPercent = Double.parseDouble(fields[1]);
                double systemPercent = Double.parseDouble(fields[3]);
                double idlePercent = Double.parseDouble(fields[7]);

                // 计算总 CPU 使用率
                double totalPercent = (userPercent + systemPercent) / cpuCount;

                // 输出结果
                System.out.printf("CPU Cores: %d, User: %.2f%%, System: %.2f%%, Idle: %.2f%%, Total: %.2f%%\n",
                        cpuCount, userPercent, systemPercent, idlePercent, totalPercent * 100);

                // 等待一段时间
                Thread.sleep(interval * 1000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
