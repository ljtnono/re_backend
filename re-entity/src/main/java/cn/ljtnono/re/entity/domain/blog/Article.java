package cn.ljtnono.re.entity.domain.blog;

import cn.ljtnono.re.entity.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 博客文章实体类
 *
 * @author Ling, Jiatong
 * Date: 2021/8/9 23:08 下午
 */
@Data
@TableName("tb_blog_article")
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String summary;

    /**
     * markdown内容
     */
    private String markdownContent;

    /**
     * 是否删除
     *
     * 1 已删除
     * 0 正常
     */
    @TableField("is_deleted")
    private Integer deleted;

    /**
     * 博客类型id
     */
    private Integer typeId;

    /**
     * 博客作者id
     */
    private Integer userId;

    /**
     * 博客封面url
     */
    private String coverUrl;

    /**
     * 博客喜欢数
     */
    private Integer view;

    /**
     * 是否是草稿
     *
     * 0 不是
     * 1 是
     */
    @TableField("is_draft")
    private Integer draft;

    /**
     * 是否推荐文章
     *
     * 0 不是
     * 1 是
     */
    @TableField("is_recommend")
    private Integer recommend;
}
