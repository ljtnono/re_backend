package cn.lingjiatong.re.service.article.config;

import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.SaUserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户上下文过滤器
 *
 * 从 Feign 透传的 X-User-Username / X-User-Id 请求头读取用户信息
 * 存入 SaUserUtils ThreadLocal
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String username = request.getHeader("X-User-Username");
            if (StringUtils.hasLength(username)) {
                User user = new User();
                user.setUsername(username);
                String userIdStr = request.getHeader("X-User-Id");
                if (StringUtils.hasLength(userIdStr)) {
                    try {
                        user.setId(Long.valueOf(userIdStr));
                    } catch (NumberFormatException e) {
                        log.debug("解析 X-User-Id 失败: {}", userIdStr);
                    }
                }
                SaUserUtils.setCurrentUser(user);
            }
            filterChain.doFilter(request, response);
        } finally {
            SaUserUtils.clear();
        }
    }
}
