package cn.lingjiatong.re.service.article.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章实体类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:22
 */
@Data
@TableName(value = "article", schema = "re_article")
public class Article {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    private String summary;

    /**
     * 文章markdown格式内容
     */
    private String markdownContent;

    /**
     * 文章html格式内容
     */
    private String htmlContent;

    /**
     * 文章所属类型id
     */
    private Long categoryId;

    /**
     * 文章作者id
     */
    private Long userId;

    /**
     * 文章封面图链接地址
     */
    private String coverUrl;

    /**
     * 文章浏览量
     */
    private Long view;

    /**
     * 文章喜欢数
     */
    private Long favorite;

    /**
     * 是否推荐
     * 0 不是 1 是
     */
    @TableField("is_recommend")
    private Byte recommend;

    /**
     * 是否置顶
     * 0 不是 1 是
     */
    @TableField("is_top")
    private Byte top;

    /**
     * 创作类型
     * 1 原创 2 转载
     */
    private Byte creationType;

    /**
     * 转载信息
     */
    private String transportInfo;

    /**
     * 引用信息
     */
    private String quoteInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     * 1 删除  0正常
     */
    @TableField("is_deleted")
    private Byte deleted;

    /**
     * 操作用户
     */
    private String optUser;
}
