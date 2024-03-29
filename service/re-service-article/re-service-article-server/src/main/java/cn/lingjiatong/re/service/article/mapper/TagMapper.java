package cn.lingjiatong.re.service.article.mapper;

import cn.lingjiatong.re.service.article.api.vo.FrontendTagListVO;
import cn.lingjiatong.re.service.article.bo.ArticleTagListBO;
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

    //********************************新增类接口********************************//


    //********************************删除类接口********************************//

    //********************************修改类接口********************************//

    //********************************查询类接口********************************//

    /**
     * 根据文章id获取文章所有的标签列表
     * 不考虑已经删除的标签
     *
     * @param articleId 文章id
     * @return 文章标签列表
     */
    List<String> findFrontendTagListByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据文章id列表查询文章的标签名列表
     *
     * @param articleIdList 文章id列表
     * @return 获取文章标签列表BO对象列表
     */
    List<ArticleTagListBO> findTagNameListByArticleIdList(@Param("articleIdList") List<Long> articleIdList);

    /**
     * 获取前端热门标签列表数据
     *
     * @return 前端博客标签列表VO对象列表
     */
    List<FrontendTagListVO> findFrontendHotTagList();

}
