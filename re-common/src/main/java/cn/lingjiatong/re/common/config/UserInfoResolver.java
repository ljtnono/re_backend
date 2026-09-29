package cn.lingjiatong.re.common.config;

import cn.lingjiatong.re.common.entity.User;

/**
 * 用户信息解析器
 *
 * 由具体业务服务实现并注册为Spring Bean，
 * 用于将网关透传的用户名解析为完整的User对象（补齐id等字段）。
 * 未实现该接口的服务将退化为仅包含username（及X-User-Id请求头）的User对象。
 *
 * @author Ling, Jiatong
 */
public interface UserInfoResolver {

    /**
     * 根据用户名解析完整用户
     *
     * @param username 用户名
     * @return 完整用户对象，解析失败返回null
     */
    User resolve(String username);
}
