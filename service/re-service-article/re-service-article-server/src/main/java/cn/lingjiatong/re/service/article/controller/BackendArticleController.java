package cn.lingjiatong.re.service.article.controller;

import cn.lingjiatong.re.common.ResultVO;
import cn.lingjiatong.re.service.article.api.dto.BackendArticleSaveDTO;
import cn.lingjiatong.re.service.article.service.BackendArticleServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 后台文章模块controller层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/17 21:56
 */
@RestController
public class BackendArticleController {

    @Autowired
    private BackendArticleServcie backendArticleServcie;

    /**
     * 后端保存文章接口
     *
     * @param dto 后台保存文章接口DTO对象
     */
    @PostMapping("/api/v1/saveArticle")
    public void saveArticle(@RequestBody BackendArticleSaveDTO dto) {
        backendArticleServcie.saveArticle(dto);
    }

    /**
     * 后端保存文章封面图接口
     *
     * @param title 文章标题
     * @param multipartFile 图片文件
     */
    @PostMapping("/api/v1/saveArticleCoverImage")
    public ResultVO<String> saveArticleCoverImage(String title, @RequestParam("file") MultipartFile multipartFile) {
        return ResultVO.success(backendArticleServcie.saveArticleCoverImage(title, multipartFile));
    }



    /**
     * 后端保存文章草稿
     * 默认保存到redis中
     *
     * @param draftTitle 草稿标题
     * @param markdownContent 文章草稿markdown内容
     */
    @PostMapping("/api/v1/saveDraftArticle")
    public void saveDraftArticle(String draftTitle, String markdownContent) {
        backendArticleServcie.saveDraftArticle(draftTitle, markdownContent);
    }

}
