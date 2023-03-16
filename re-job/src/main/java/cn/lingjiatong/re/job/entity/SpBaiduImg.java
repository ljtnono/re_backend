package cn.lingjiatong.re.job.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 百度图片爬虫数据实体类
 *
 * @author Ling, Jiatong
 * Date: 2022/10/14 00:53
 */
@Data
@TableName(value = "sp_baidu_img", schema = "re_spider")
public class SpBaiduImg {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 图片标题
     */
    private String title;

    /**
     * 图片链接地址
     */
    private String src;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片格式
     */
    private String format;

    /**
     * md5标识
     * 此字段由 title + src + width + height + format计算而成
     */
    private String uniqueMd5;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录最后修改时间
     */
    private LocalDateTime modifyTime;

}
