package cn.lingjiatong.re.service.sys.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.util.FormatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Ling, Jiatong
 * Date: 2023/4/3 17:15
 */
public class MemoryInfo {

    public static void main(String[] args) throws JSchException, IOException {
        String hostname = "www.lingjiatong.cn";
        String username = "root";
        String password = "ljtLJT715336";

        String command = "free -m";
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, 8007);
        session.setPassword(password);
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

        System.out.println("Total Memory: " + totalMemory);
        System.out.println("Used Memory: " + usedMemory);
        System.out.println("Free Memory: " + freeMemory);
        System.out.println("memoryUsage: " + memoryUsage);

    }

}
