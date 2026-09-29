package cn.lingjiatong.re.service.sys.config;

import cn.lingjiatong.re.common.config.UserInfoResolver;
import cn.lingjiatong.re.service.sys.service.BackendUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户上下文配置类
 *
 * 将网关透传的用户名解析为包含id、角色、权限的完整用户对象，
 * 供re-common的通用用户上下文过滤器（CommonUserContextFilter）使用。
 *
 * 注意：此类必须独立于SpringBeanConfig，
 * 避免与MyBatis自动配置产生循环依赖
 *
 * @author Ling, Jiatong
 */
@Configuration
public class UserContextConfig {

    @Autowired
    private BackendUserService backendUserService;

    @Bean
    public UserInfoResolver userInfoResolver() {
        return backendUserService::getCurrentUserByUsername;
    }
}
