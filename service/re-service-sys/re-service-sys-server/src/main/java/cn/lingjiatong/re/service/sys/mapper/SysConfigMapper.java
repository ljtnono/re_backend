package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigAddDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigFindDTO;
import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigUpdateDTO;
import cn.lingjiatong.re.service.sys.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统配置mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/9/18 01:43
 */
public interface SysConfigMapper extends BaseMapper<SysConfig> {


    /**
     * 获取前端站点配置信息
     *
     * @param dto 获取前端站点配置DTO对象
     * @return 系统配置实体类列表
     */
    List<SysConfig> findFrontendWebsiteConfig(@Param("dto") FrontendWebsiteConfigFindDTO dto);

    /**
     * 新增前端站点配置信息
     *
     * @param list 新增前端站点配置DTO对象列表
     */
    void addFrontendWebsiteConfig(@Param("list") List<FrontendWebsiteConfigAddDTO> list);


    /**
     * 更新前端站点配置信息
     *
     * @param list 更新前端站点配置DTO对象列表
     */
    void updateFrontendWebsiteConfig(@Param("list") List<FrontendWebsiteConfigUpdateDTO> list);

}
