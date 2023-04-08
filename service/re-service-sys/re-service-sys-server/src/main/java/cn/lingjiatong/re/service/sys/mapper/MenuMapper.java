package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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
     * 分页获取菜单列表
     *
     * @param page 分页对象
     * @param dto 后台菜单列表DTO对象
     * @return 后台菜单列表VO对象列表
     */
    Page<BackendMenuListVO> findMenuList(Page<?> page, @Param("dto") BackendMenuListDTO dto);

    /**
     * 分页获取菜单列表-获取总数
     *
     * @param dto 后台菜单列表DTO对象
     * @return 分页获取菜单列表总数
     */
    long findMenuListTotal(@Param("dto") BackendMenuListDTO dto);

}
