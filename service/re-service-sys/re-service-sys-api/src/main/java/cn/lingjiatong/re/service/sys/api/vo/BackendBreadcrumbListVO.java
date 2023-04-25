package cn.lingjiatong.re.service.sys.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 后台获取面包屑导航列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 4/22/23 3:18 PM
 */
@Data
@Schema(name = "BackendBreadcrumbListVO", description = "后台获取面包屑导航列表VO对象")
public class BackendBreadcrumbListVO {

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 面包屑导航列表
     */
    private List<String> breadcrumbList;
}
