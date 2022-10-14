package cn.lingjiatong.re.job.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 头条热榜爬虫数据实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 21:26
 */
@Data
@TableName("sp_toutiao_rb")
@Document(indexName = "sp_toutiao_rb")
public class SpToutiaoRb {

    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @Field(name = "title", type = FieldType.Text)
    private String title;

    /**
     * 链接
     */
    @Field(name = "link", type = FieldType.Text)
    private String link;

    /**
     * 热度
     */
    @Field(name = "hotValue", type = FieldType.Long)
    private Long hotValue;

    /**
     * 标志
     * 0 无标志
     * 1 热榜
     * 2 新闻
     */
    @Field(name = "state", type = FieldType.Byte)
    private Byte state;

    /**
     * 查询关键字 以空格隔开
     */
    @Field(name = "queryWord", type = FieldType.Text)
    private String queryWord;

    /**
     * md5标识
     * 此字段由 title + link + hotValue + state + queryWord计算而成
     */
    @Field(name = "uniqueMd5", type = FieldType.Text)
    private String uniqueMd5;

    /**
     * 记录创建时间
     */
    @Field(name = "createTime", type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime;

    /**
     * 记录最后修改时间
     */
    @Field(name = "modifyTime", type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime modifyTime;
}
