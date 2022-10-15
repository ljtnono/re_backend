package cn.lingjiatong.re.service.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 头条热榜爬虫数据实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 21:26
 */
@Data
@TableName("sp_toutiao_rb")
public class SpToutiaoRb {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 链接
     */
    private String link;

    /**
     * 热度
     */
    private Long hotValue;

    /**
     * 标志
     * 0 无标志
     * 1 热榜
     * 2 新闻
     */
    private Byte state;

    /**
     * 查询关键字 以空格隔开
     */
    private String queryWord;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后修改时间
     */
    private LocalDateTime modifyTime;
}
