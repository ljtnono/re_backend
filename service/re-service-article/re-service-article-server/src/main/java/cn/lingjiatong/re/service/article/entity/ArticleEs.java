package cn.lingjiatong.re.service.article.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 保存在es中的文章实体
 *
 * @author Ling, Jiatong
 * Date: 2022/10/20 20:55
 */
@Data
@Document(indexName = "article")
public class ArticleEs {

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    /**
     * 文章简介
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String summary;

    /**
     * 文章markdown格式内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String markdownContent;

    /**
     * 文章html格式内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String htmlContent;

    /**
     * 文章所属类型id
     */
    @Field(type = FieldType.Long)
    private Integer categoryId;

    /**
     * 文章作者id
     */
    @Field(type = FieldType.Long)
    private Integer userId;

    /**
     * 文章封面图链接地址
     */
    private String coverUrl;

    /**
     * 文章浏览量
     */
    @Field(type = FieldType.Long)
    private Long view;

    /**
     * 文章喜欢数
     */
    @Field(type = FieldType.Long)
    private Long favorite;

    /**
     * 是否推荐
     * 0 不是 1 是
     */
    @Field(type = FieldType.Byte)
    private Byte recommend;

    /**
     * 是否置顶
     * 0 不是 1 是
     */
    @Field(type = FieldType.Byte)
    private Byte top;

    /**
     * 创作类型
     * 1 原创 2 转载
     */
    @Field(type = FieldType.Byte)
    private Byte creationType;

    /**
     * 转载信息
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String transportInfo;

    /**
     * 引用信息
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String quoteInfo;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     * 1 删除  0正常
     */
    @Field(type = FieldType.Byte)
    private Byte deleted;

    /**
     * 操作用户
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String optUser;

    /**
     * 博客的标签列表
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private List<String> tagList;
}
