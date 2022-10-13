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

    public static void main(String[] args) {
        System.out.println(getUrlQueryParamter("https://img0.baidu.com/it/u=2674201749,821380200&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500"));
    }
}
