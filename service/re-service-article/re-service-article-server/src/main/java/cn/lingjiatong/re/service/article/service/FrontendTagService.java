package cn.lingjiatong.re.service.article.service;

import cn.lingjiatong.re.service.article.api.vo.FrontendTagListVO;
import cn.lingjiatong.re.service.article.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 前端博客标签模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/16 12:28
 */
@Slf4j
@Service
public class FrontendTagService {

    @Resource
    private TagMapper tagMapper;

    // ********************************新增类接口********************************

    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端热门博客标签列表
     *
     * @return 前端博客标签列表VO对象列表
     */
    public List<FrontendTagListVO> findFrontendHotTagList() {

        return null;
    }
}
