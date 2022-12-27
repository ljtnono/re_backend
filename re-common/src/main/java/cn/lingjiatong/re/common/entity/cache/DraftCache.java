package cn.lingjiatong.re.common.entity.cache;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章草稿缓存对象
 *
 * @author Ling, Jiatong
 * Date: 2022/12/27 09:35
 */
@Data
public class DraftCache {

    /**
     * 草稿id
     */
    private String draftId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 草稿的内容
     */
    private String markdownContent;

    /**
     * 草稿保存时间
     */
    private LocalDateTime saveTime;
}
