package cn.lingjiatong.re.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * url工具类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/14 01:11
 */
@Slf4j
public class UrlUtil {

    /**
     * 获取url中的请求参数
     * 即?后面的参数
     *
     * @return 请求参数参数健值对
     */
    public static Map<String, String> getUrlQueryParamter(String url) {
        if (StringUtils.isEmpty(url)) {
            return Collections.emptyMap();
        }
        URL u;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            log.error("==========url格式错误");
            return Collections.emptyMap();
        }
        // 获取参数字符串
        String query = u.getQuery();
        if (StringUtils.isEmpty(query)) {
            // 参数不存在
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<>();
        String[] split = query.split("&");
        for (String p : split) {
            String[] kv = p.split("=");
            result.put(kv[0], kv[1]);
        }
        return result;
    }

    /**
     * 移除url后面的参数部分
     *
     * @param url 原url
     * @return 移除了参数部分的url
     */
    public static String removeUrlParamter(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        try {
            URL u = new URL(url);
            return u.getProtocol() + "://" + u.getHost() + ":" + u.getPort() + u.getPath();
        } catch (MalformedURLException e) {
            log.error("==========url格式错误");
            return "";
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        URL u = new URL("http://www.lingjiatong.cn:30090/rootelement/articleCover/Snipaste_2022-10-18_20-51-42.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=lingjiatong%2F20221018%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20221018T130423Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=ef5746e2bf41848f9b0f649b4f6303df156311dd6b796cbce9ecea0f7fc42d74");

        System.out.println(getUrlQueryParamter("https://img0.baidu.com/it/u=2674201749,821380200&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500"));
    }
}
