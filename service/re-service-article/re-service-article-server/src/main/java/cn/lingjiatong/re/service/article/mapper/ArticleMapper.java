package cn.lingjiatong.re.service.article.mapper;

import cn.lingjiatong.re.service.article.api.dto.BackendArticleListDTO;
import cn.lingjiatong.re.service.article.api.dto.FrontendArticleScrollDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.api.vo.FrontendArticleScrollVO;
import cn.lingjiatong.re.service.article.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 博客文章模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:44
 */
public interface ArticleMapper extends BaseMapper<Article> {

    //********************************新增类接口********************************//

    //********************************删除类接口********************************//

    //********************************修改类接口********************************//

    //********************************查询类接口********************************//

    /**
     * 分页获取文章列表
     *
     * @param dto 后端获取文章列表DTO对象
     * @param page 分页对象
     * @return 后端获取文章列表VO对象分页对象
     */
    Page<BackendArticleListVO> findBackendArticleList(Page<?> page, @Param("dto") BackendArticleListDTO dto);

    /**
     * 分页获取文章列表-查询总数
     *
     * @param dto 后端获取文章列表DTO对象
     * @return 分页获取文章列表-总数
     */
    long findBackendArticleListTotal(@Param("dto") BackendArticleListDTO dto);

    /**
     * 前端滚动获取文章列表
     *
     * @param dto 前端文章无限滚动列表DTO对象
     * @param page 分页对象
     * @return 前端文章无限滚动列表VO对象分页对象
     */
    Page<FrontendArticleScrollVO> findFrontendArticleScroll(Page<?> page, @Param("dto") FrontendArticleScrollDTO dto);

    /**
     * 前端滚动获取文章列表-查询总数
     *
     * @return 分页获取文章列表-总数
     */
    long findFrontendArticleScrollTotal();

}
