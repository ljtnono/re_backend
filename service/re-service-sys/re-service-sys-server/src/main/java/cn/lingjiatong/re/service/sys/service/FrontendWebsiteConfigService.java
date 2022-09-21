package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigVO;
import cn.lingjiatong.re.service.sys.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站点配置service层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:52
 */
@Slf4j
@Service
public class WebsiteConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    // ********************************新增类接口********************************


    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端
     *
     * @param dto
     * @return
     */
    public FrontendWebsiteConfigVO findFrontendWebsiteConfig(FrontendWebsiteConfigDTO dto) {
        FrontendWebsiteConfigVO vo = new FrontendWebsiteConfigVO();

        return vo;
    }

    // ********************************私有函数********************************


    // ********************************公用函数********************************

}
