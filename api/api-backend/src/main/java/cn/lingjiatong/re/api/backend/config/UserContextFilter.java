package cn.lingjiatong.re.api.backend.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.util.SaUserUtils;
import cn.lingjiatong.re.service.sys.api.client.BackendUserFeignClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户上下文过滤器
 *
 * 从 Authorization 请求头解析 JWT → 查库获取 User → 存入 SaUserUtils ThreadLocal
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Lazy
    @Autowired
    private BackendUserFeignClient backendUserFeignClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(CommonConstant.TOKE_HTTP_HEADER);
            if (StringUtils.hasLength(authHeader)) {
                String token = authHeader;
                if (authHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
                    token = authHeader.substring(CommonConstant.TOKEN_PREFIX.length());
                }
                Object loginId = StpUtil.getLoginIdByToken(token);
                if (loginId != null) {
                    String username = loginId.toString();
                    User user = new User();
                    user.setUsername(username);
                    try {
                        ResultVO<User> result = backendUserFeignClient.getCurrentUserByUsername(username);
                        if (result != null && result.getData() != null) {
                            user = result.getData();
                        }
                    } catch (Exception e) {
                        log.debug("查询用户完整信息失败，使用仅username的User: {}", e.getMessage());
                    }
                    SaUserUtils.setCurrentUser(user);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            SaUserUtils.clear();
        }
    }
}
