package cn.lingjiatong.re.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * PathMatcher处理工具
 *
 * @author Ling, Jiatong
 * Date: 2023/1/2 21:51
 */
@Slf4j
public class PathMatcherUtil {

    /**
     * 工具类不允许实例化
     */
    private PathMatcherUtil() {
    }

    public static PathMatcherUtil getInstance() {
        return PathMatcherUtil.Holder.INSTANCE;
    }

    public static class Holder {
        private final static PathMatcherUtil INSTANCE = new PathMatcherUtil();
    }

    /**
     * 对比请求路径是否匹配
     *
     * @param matcher 匹配器
     * @param pattern 正则
     * @param reqPath 请求路径
     * @return true 匹配成功 false 匹配失败
     */
    public boolean match(PathMatcher matcher, String pattern, String reqPath) {
        return matcher.match(pattern, reqPath);
    }

    /**
     * 获取请求路径中的PathVariable
     *
     * @param matcher 匹配器
     * @param pattern 正则
     * @param reqPath 请求路径
     * @return 请求路径中的PathVariable
     */
    public Map<String, String> extractUriTemplateVariables(PathMatcher matcher, String pattern, String reqPath) {
        Map<String, String> variablesMap = matcher.extractUriTemplateVariables(pattern, reqPath);
        return variablesMap;
    }

}
