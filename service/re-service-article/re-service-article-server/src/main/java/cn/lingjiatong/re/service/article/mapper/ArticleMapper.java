package cn.lingjiatong.re.service.article.mapper;

import cn.lingjiatong.re.service.article.api.dto.BackendArticleListDTO;
import cn.lingjiatong.re.service.article.api.vo.BackendArticleListVO;
import cn.lingjiatong.re.service.article.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 博客文章模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 11:44
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页获取文章列表
     *
     * @param dto 后端获取文章列表DTO对象
     * @param page 分页对象
     * @return 后端获取文章列表VO对象分页对象
     */
    Page<BackendArticleListVO> findArticleList(Page<?> page, @Param("dto") BackendArticleListDTO dto);
}
