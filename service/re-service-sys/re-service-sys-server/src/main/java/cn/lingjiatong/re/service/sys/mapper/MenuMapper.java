package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2023/2/7 09:52
 */
public interface MenuMapper extends BaseMapper<Menu> {

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取菜单列表
     *
     * @param dto 后台菜单列表DTO对象
     * @return 后台菜单列表VO对象列表
     */
    List<BackendMenuListVO> findMenuList(@Param("dto") BackendMenuListDTO dto);

}
