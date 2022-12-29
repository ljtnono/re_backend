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

    static List<Byte> getDeleteStatusCodeList() {
        return List.of(ENTITY_DELETE, ENTITY_NORMAL);
    }

    // token的http请求头
    String TOKE_HTTP_HEADER = "Authorization";
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
}
