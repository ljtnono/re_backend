package cn.lingjiatong.re.service.sys.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Ling, Jiatong
 * Date: 2023/4/3 17:51
 */
public class RemoteDiskUsage {

    public static void main(String[] args) {
        String host = "www.lingjiatong.cn";
        String user = "root";
        String password = "ljtLJT715336";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 8007);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            String command = "df -h";
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();

            InputStream in = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("Filesystem")) {
                    String[] fields = line.split("\\s+");
                    String filesystem = fields[0];
                    String size = fields[1];
                    String used = fields[2];
                    String available = fields[3];
                    String usePercentage = fields[4];
                    String mountPoint = fields[5];
                    System.out.println("Filesystem: " + filesystem);
                    System.out.println("Size: " + size);
                    System.out.println("Used: " + used);
                    System.out.println("Available: " + available);
                    System.out.println("Use%: " + usePercentage);
                    System.out.println("Mounted on: " + mountPoint);
                }
            }

            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
