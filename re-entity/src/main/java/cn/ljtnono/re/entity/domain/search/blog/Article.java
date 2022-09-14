package cn.ljtnono.re.entity.domain.search.blog;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * 博客索引实体类
 *
 * @author Ling, Jiatong
 * Date: 2021/8/9 11:37 下午
 */
@Data
@Document(indexName = "blog_article")
@Setting(shards = 3)
public class Article {

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    /**
     * 简介
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String summary;

    /**
     * markdown内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String markdownContent;

}
