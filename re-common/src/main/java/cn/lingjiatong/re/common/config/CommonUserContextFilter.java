package cn.lingjiatong.re.common.config;

import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.SaUserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 通用用户上下文过滤器（所有扫描了cn.lingjiatong.re.common的服务自动生效）
 *
 * 从网关透传的 X-User-Username 请求头读取用户名，构建User对象存入 SaUserUtils ThreadLocal。
 *
 * 构建策略：
 * 1. 若服务实现了 UserInfoResolver 接口并注册为Bean，调用其解析完整用户（可补齐id等字段）
 * 2. 否则退化为读取 X-User-Id / X-User-Username 请求头拼装基础User对象
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Component
public class CommonUserContextFilter extends OncePerRequestFilter {

    /**
     * 用户信息解析器，各服务可选实现
     */
    @Autowired(required = false)
    private UserInfoResolver userInfoResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String username = request.getHeader("X-User-Username");
            if (StringUtils.hasLength(username)) {
                User user = null;
                if (userInfoResolver != null) {
                    try {
                        user = userInfoResolver.resolve(username);
                    } catch (Exception e) {
                        log.error("UserInfoResolver解析用户信息失败, username: {}", username, e);
                    }
                }
                if (user == null) {
                    // 兜底：仅用请求头拼装基础User对象
                    user = new User();
                    user.setUsername(username);
                    String userIdStr = request.getHeader("X-User-Id");
                    if (StringUtils.hasLength(userIdStr)) {
                        try {
                            user.setId(Long.valueOf(userIdStr));
                        } catch (NumberFormatException e) {
                            log.debug("解析 X-User-Id 失败: {}", userIdStr);
                        }
                    }
                }
                user.setPassword(null);
                SaUserUtils.setCurrentUser(user);
            }
            filterChain.doFilter(request, response);
        } finally {
            SaUserUtils.clear();
        }
    }
}
