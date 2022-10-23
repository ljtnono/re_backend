package cn.lingjiatong.re.auth.component;

import cn.lingjiatong.re.auth.config.JwtUtil;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * token校验过滤器
 *
 * <p>
 * 此过滤器会放在 UsernamePasswordAuthenticationFilter 过滤器之前，主要是为了在spring security校验凭证之前将token放入到上下文环境中。
 * 本过滤器中抛出的异常也会被spring security过滤器链捕获。
 * </p>
 *
 * @author Ling, Jiatong
 * Date: 2020/7/7 20:36 下午
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(CommonConstant.TOKE_HTTP_HEADER);
        String username = null;
        // 从header中取token
        if (StringUtils.hasLength(token) && token.startsWith(CommonConstant.TOKEN_PREFIX)) {
            // 获取到用户名
            token = token.substring(CommonConstant.TOKEN_PREFIX.length());
            username = jwtUtil.getUsernameFromToken(token);
        }
        // 如果header中没有，那么从url中取
        if (!StringUtils.hasLength(token)) {
            token = request.getParameter(CommonConstant.TOKE_HTTP_HEADER);
            if (StringUtils.hasLength(token) && token.startsWith(CommonConstant.TOKEN_PREFIX)) {
                token = URLDecoder.decode(token.substring(CommonConstant.TOKEN_PREFIX.length()), StandardCharsets.UTF_8);
                username = jwtUtil.getUsernameFromToken(token);
            }
        }
        // 如果url中没有，从Cookie中取
        if (!StringUtils.hasLength(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (CommonConstant.TOKEN_COKKIE_HEADER.equalsIgnoreCase(cookie.getName())) {
                        token = cookie.getValue().substring(CommonConstant.TOKEN_COKKIE_HEADER.length());
                        username = jwtUtil.getUsernameFromToken(token);
                    }
                }
            }
        }

        // 第二次携带token过来，此时token存在于redis中，但是在spring security的环境中不存在，所以要先校验并生成spring security中的凭证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 检验Token是否合法
            User user = (User) userDetailsService.loadUserByUsername(username);
            // 验证token是否超过会话超时时间，如果超过，那么将token删除
            if (jwtUtil.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
