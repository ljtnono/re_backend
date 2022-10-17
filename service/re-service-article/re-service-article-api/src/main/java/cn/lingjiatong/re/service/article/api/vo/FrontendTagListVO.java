package cn.lingjiatong.re.service.article.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 前端博客标签列表VO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:15
 */
@Data
@ApiModel(description = "前端博客标签列表VO对象")
public class FrontendTagListVO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String name;
}
