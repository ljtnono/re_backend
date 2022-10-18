package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 博客文章实体类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:22
 */
@Data
@TableName("article")
@Document(indexName = "article")
public class Article {

    /**
     * 主键id
     */
    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    /**
     * 文章简介
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String summary;

    /**
     * 文章markdown格式内容
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String markdownContent;

    /**
     * 文章html格式内容
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String htmlContent;

    /**
     * 文章所属类型id
     */
    @Field(type = FieldType.Integer, analyzer = "")
    private Integer categoryId;

    /**
     * 文章作者id
     */
    @Field(type = FieldType.Integer, analyzer = "")
    private Integer userId;

    /**
     * 文章封面图链接地址
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String coverUrl;

    /**
     * 文章浏览量
     */
    @Field(type = FieldType.Long, analyzer = "")
    private Long view;

    /**
     * 文章喜欢数
     */
    @Field(type = FieldType.Long, analyzer = "")
    private Long favorite;

    /**
     * 是否推荐
     * 0 不是 1 是
     */
    @Field(type = FieldType.Byte, analyzer = "")
    @TableField("is_recommend")
    private Byte recommend;

    /**
     * 是否置顶
     * 0 不是 1 是
     */
    @Field(type = FieldType.Byte, analyzer = "")
    @TableField("is_top")
    private Byte top;

    /**
     * 创作类型
     * 1 原创 2 转载
     */
    @Field(type = FieldType.Byte, analyzer = "")
    private Byte creationType;

    /**
     * 转载信息
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String transportInfo;

    /**
     * 引用信息
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String quoteInfo;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     * 1 删除  0正常
     */
    @TableField("is_deleted")
    @Field(type = FieldType.Byte, analyzer = "")
    private Byte deleted;

    /**
     * 操作用户
     */
    @Field(type = FieldType.Text, analyzer = "")
    private String optUser;
}
