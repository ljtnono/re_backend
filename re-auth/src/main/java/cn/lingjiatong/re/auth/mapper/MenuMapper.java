package cn.lingjiatong.re.auth.mapper;

import cn.lingjiatong.re.common.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 15:52
 */
public interface MenuMapper extends BaseMapper<Menu> {
    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 根据用户id和项目名称获取用户菜单列表
     *
     * @param userId 用户id
     * @param projectName 项目名称
     * @return 用户菜单列表
     */
    List<Menu> getMenuListByUserId(@Param("userId") Long userId, @Param("projectName") String projectName);

}
