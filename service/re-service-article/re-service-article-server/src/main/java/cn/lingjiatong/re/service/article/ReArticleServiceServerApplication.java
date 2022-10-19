package cn.lingjiatong.re.service.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 博客文章相关模块微服务启动类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:17
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.lingjiatong.re.service.article", "cn.lingjiatong.re.common"})
@EnableFeignClients(basePackages = {"cn.lingjiatong.re.service.article.api"})
@MapperScan(basePackages = {"cn.lingjiatong.re.service.article.mapper"})
public class ReArticleServiceServerApplication {


//    public static String getSummaryFromMarkdownContent( String markdownContent) {
//        StringBuilder result = new StringBuilder();
//        String[] lines = markdownContent.split("\\r?\\n");
//        if (lines.length > 0) {
//            // 如果以 # 开头则为标题，跳过该行
//            for (String line : lines) {
//                if (line.startsWith("#") || line.equals("")) {
//                    continue;
//                }
//                result.append(line).append("\n");
//                if (result.length() > 200) {
//                    break;
//                }
//            }
//        }
//        return result.toString();
//    }

    public static void main(String[] args) {
        SpringApplication.run(ReArticleServiceServerApplication.class, args);
//        FileInputStream fileInputStream = new FileInputStream("/Users/lingjiatong/document/markdown/mydoc/docs/16655384393669.md");
//        byte[] buf = new byte[1024];
//        StringBuilder stringBuilder = new StringBuilder();
//        while (fileInputStream.read(buf, 0, buf.length) != -1) {
//            stringBuilder.append(new String(buf, Charset.defaultCharset()));
//        }
//        System.out.println(getSummaryFromMarkdownContent(stringBuilder.toString()));
//        System.out.println(JSONUtil.objectToString(System.getProperties()));
    }
}
