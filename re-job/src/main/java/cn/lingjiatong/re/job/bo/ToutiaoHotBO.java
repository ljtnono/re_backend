package cn.lingjiatong.re.job.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 头条热榜BO对象
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 21:16
 */
@Data
public class ToutiaoHotBO {

    /**
     * 标题
     */
    @JsonProperty("Title")
    private String title;

    /**
     * 访问地址
     */
    @JsonProperty("Url")
    private String url;

    /**
     * 热值
     */
    @JsonProperty("HotValue")
    private String hotValue;

    /**
     * 查询关键字
     */
    @JsonProperty("QueryWord")
    private String queryWord;

    /**
     * 标志
     * 空串 代表无标志
     * new 代表新闻
     * hot 代表热搜
     */
    @JsonProperty("Label")
    private String label;
}
