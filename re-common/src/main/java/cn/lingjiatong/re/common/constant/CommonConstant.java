package cn.lingjiatong.re.common.constant;

import java.util.List;

/**
 * 通用设定常量池
 *
 * @author Ling, Jiatong
 * Date: 2022/10/18 21:25
 */
public interface CommonConstant {

    // 已删除
    Byte ENTITY_DELETE = 1;
    // 正常
    Byte ENTITY_NORMAL = 0;
    // 升序
    Byte ORDER_BY_ASC = 1;
    // 降序
    Byte ORDER_BY_DESC = 2;

    static List<Byte> deleteValues() {
        return List.of(ENTITY_DELETE, ENTITY_NORMAL);
    }

    // token的http请求头
    String TOKE_HTTP_HEADER = "Authorization";
    // websocket协议携带token的请求头
    String WEBSOCKET_TOKEN_HEADER = "Sec-WebSocket-Protocol";
    // cookie中的token键名
    String TOKEN_COOKIE_HEADER = TOKE_HTTP_HEADER;
    // token内容前缀
    String TOKEN_PREFIX = "Bearer ";
    // token加密盐值
    String TOKEN_SECRET_SALT = "||^^^-ROOTELEMENT-^^^||";
    // token密钥生成时的密码
    String TOKEN_SECRET_KEY_PASSWORD = "ROOTELEMENT";
    // token密钥名
    String TOKEN_SECRET_KEY_NAME = "re.jks";
    // token密钥别名
    String TOKEN_SECRET_KEY_ALIAS = "re";
    // re_admin 项目名
    String PROJECT_NAME_BACKEND_PAGE = "re_admin";
    // re_frontend项目名
    String PROJECT_NAME_FRONTEND_PAGE = "re_frontend";
    // 项目名称列表
    List<String> PROJECT_NAME_VALUES = List.of(PROJECT_NAME_BACKEND_PAGE, PROJECT_NAME_FRONTEND_PAGE);
}
