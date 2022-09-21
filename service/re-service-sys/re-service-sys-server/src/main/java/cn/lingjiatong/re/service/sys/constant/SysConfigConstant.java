package cn.lingjiatong.re.service.sys.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 系统配置业务常量
 *
 * @author Ling, Jiatong
 * Date: 2022/9/19 15:57
 */
public interface SysConfigConstant {

    // 获取全部站点配置
    Integer WEBSITE_ACQUIRE_TYPE_ALL = 1;

    // 获取部分站点配置
    Integer WEBSITE_ACQUIRE_TYPE_MULTIPLE = 2;

    // 站点配置的所有key列表
    List<String> WEBSITE_KEY_LIST = Arrays.asList(
            "HEADER_LOGO_URL",
            "NICK_NAME",
            "AVATAR_URL",
            "ABOUT_AUTHOR",
            "SEND_ME_EMAIL",
            "GITHUB_AUTHOR",
            "FOOTER_COPYRIGHT",
            "FOOTER_DRIVER",
            "AUTHOR_WX_QRCODE_URL",
            "FOOTER_ABOUT_WEBSITE",
            "WEBSITE_ICP_CODE",
            "GITHUB_WEBSITE",
            "AUTHOR_WX",
            "AUTHOR_QQ",
            "AUTHOR_GITHUB_USERNAME",
            "AUTHOR_WX_PAY_QRCODE_URL",
            "AUTHOR_ALIPAY_PAY_QRCODE_URL"
    );

}
