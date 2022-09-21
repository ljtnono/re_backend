package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigVO;
import cn.lingjiatong.re.service.sys.constant.SysConfigConstant;
import cn.lingjiatong.re.service.sys.entity.SysConfig;
import cn.lingjiatong.re.service.sys.mapper.SysConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 前端站点配置service层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/17 18:52
 */
@Slf4j
@Service
public class FrontendWebsiteConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    // ********************************新增类接口********************************


    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端站点设置
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    public FrontendWebsiteConfigVO findFrontendWebsiteConfig(FrontendWebsiteConfigDTO dto) {
        // 校验是否为Null
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR));
        if (!Arrays.asList(SysConfigConstant.WEBSITE_ACQUIRE_TYPE_ALL, SysConfigConstant.WEBSITE_ACQUIRE_TYPE_MULTIPLE).contains(dto.getAcquireType())) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR);
        }
        // 如果是获取部分，则列表不为空
        if (SysConfigConstant.WEBSITE_ACQUIRE_TYPE_MULTIPLE.equals(dto.getAcquireType())) {
            if (CollectionUtils.isEmpty(dto.getAcquireKeyList())) {
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR);
            }
        }

        FrontendWebsiteConfigVO vo = new FrontendWebsiteConfigVO();
        List<SysConfig> frontendWebsiteConfig = sysConfigMapper.findFrontendWebsiteConfig(dto);
        Map<String, String> map = new HashMap<>(frontendWebsiteConfig.size());
        frontendWebsiteConfig.forEach(config -> {
            map.put(config.getKey(), config.getValue());
        });
        vo.setValues(map);
        return vo;
    }

    // ********************************私有函数********************************


    // ********************************公用函数********************************

}
