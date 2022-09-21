package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.service.sys.api.dto.FrontendWebsiteConfigDTO;
import cn.lingjiatong.re.service.sys.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<SysConfig> findFrontendWebsiteConfig(@Param("dto") FrontendWebsiteConfigDTO dto);

}
