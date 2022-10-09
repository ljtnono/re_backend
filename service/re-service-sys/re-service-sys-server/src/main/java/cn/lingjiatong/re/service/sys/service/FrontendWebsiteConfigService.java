package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ParamErrorException;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigAddDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigUpdateDTO;
import cn.lingjiatong.re.service.sys.api.vo.FrontendWebsiteConfigAddVO;
import cn.lingjiatong.re.service.sys.constant.SysConfigConstant;
import cn.lingjiatong.re.service.sys.constant.SysConfigErrorMessageConstant;
import cn.lingjiatong.re.service.sys.constant.SysConfigRegexConstant;
import cn.lingjiatong.re.service.sys.entity.SysConfig;
import cn.lingjiatong.re.service.sys.mapper.SysConfigMapper;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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


    /**
     * 新增前端站点
     *
     * @param list 新增前端站点配置DTO对象列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFrontendWebsiteConfig(List<FrontendWebsiteConfigAddDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR);
        }

        // 参数检查
        list.forEach(dto -> {
            String description = dto.getDescription();
            String key = dto.getKey();
            String value = dto.getValue();
            if (StringUtils.isEmpty(description)) {
                dto.setDescription("无");
            }
            if (StringUtils.isEmpty(key)) {
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), SysConfigErrorMessageConstant.SYS_CONFIG_KEY_NOT_EMPTY_MESSAGE);
            }
            if (StringUtils.isEmpty(value)) {
                dto.setValue("无");
            }
            // 系统配置格式检查
            if (!SysConfigRegexConstant.SYS_CONFIG_KEY_REGEX.matcher(key).matches()) {
                throw new ParamErrorException(ErrorEnum.REQUEST_PARAM_ERROR.getCode(), SysConfigErrorMessageConstant.SYS_CONFIG_KEY_FORMAT_MESSAGE);
            }
        });

        // 先查出数据库所有的配置列表
        FrontendWebsiteConfigFindDTO frontendWebsiteConfigFindDTO = new FrontendWebsiteConfigFindDTO();
        frontendWebsiteConfigFindDTO.setAcquireType(SysConfigConstant.WEBSITE_ACQUIRE_TYPE_ALL);
        List<SysConfig> frontendWebsiteConfigList = sysConfigMapper.findFrontendWebsiteConfig(frontendWebsiteConfigFindDTO);
        List<String> sysConfigKeyList = frontendWebsiteConfigList.stream().map(SysConfig::getKey).collect(Collectors.toList());
        List<FrontendWebsiteConfigAddDTO> addList = Lists.newArrayList();
        List<FrontendWebsiteConfigUpdateDTO> updateList = Lists.newArrayList();

        // 对健值对拆分，拆分成数据库中已存在的和数据库中不存在的。
        list.forEach(dto -> {
            if (sysConfigKeyList.contains(dto.getKey())) {
                FrontendWebsiteConfigUpdateDTO updateDTO = new FrontendWebsiteConfigUpdateDTO();
                BeanUtils.copyProperties(dto, updateDTO);
                updateList.add(updateDTO);
            } else {
                addList.add(dto);
            }
        });

        // 新增前端配置
        if (CollectionUtils.isEmpty(addList)) {
            sysConfigMapper.addFrontendWebsiteConfig(addList);
        }

        // 更新前端配置
        if (CollectionUtils.isEmpty(updateList)) {
            sysConfigMapper.updateFrontendWebsiteConfig(updateList);
        }

    }


    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端站点设置
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 获取前端站点配置VO对象
     */
    public FrontendWebsiteConfigAddVO findFrontendWebsiteConfig(FrontendWebsiteConfigFindDTO dto) {
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

        FrontendWebsiteConfigAddVO vo = new FrontendWebsiteConfigAddVO();
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
