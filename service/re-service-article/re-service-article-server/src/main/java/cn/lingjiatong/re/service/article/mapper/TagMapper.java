package cn.lingjiatong.re.service.article.mapper;

import cn.lingjiatong.re.service.article.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 博客标签模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:45
 */
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 根据文章id获取文章所有的标签列表
     * 不考虑已经删除的标签
     *
     * @param articleId 文章id
     * @return 文章标签列表
     */
    List<String> findTagListByArticleId(@Param("articleId") Long articleId);
}
