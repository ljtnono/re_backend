package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.TrRoleMenu;
import cn.lingjiatong.re.service.sys.mapper.TrRoleMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联模块service层
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 10:04 PM
 */
@Service
public class TrRoleMenuService {

    @Autowired
    private TrRoleMenuMapper trRoleMenuMapper;

    // ********************************新增类接口********************************

    /**
     * 插入角色菜单关联信息
     *
     * @param trRoleMenu 角色菜单关联实体
     */
    public void saveTrRoleMenu(TrRoleMenu trRoleMenu) {
        trRoleMenuMapper.insert(trRoleMenu);
    }

    // ********************************删除类接口********************************



    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 根据角色id获取角色对应的菜单id列表
     *
     * @param roleId 角色id
     * @return 菜单id列表
     */
    public List<Long> findMenuIdListByRoleId(Long roleId) {
        List<TrRoleMenu> trRoleMenuList = trRoleMenuMapper.selectList(new LambdaQueryWrapper<TrRoleMenu>()
                .select(TrRoleMenu::getMenuId)
                .eq(TrRoleMenu::getRoleId, roleId));
        if (CollectionUtils.isEmpty(trRoleMenuList)) {
            return Lists.newArrayList();
        }
        return trRoleMenuList
                .stream()
                .map(TrRoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    // ********************************私有函数********************************

    // ********************************公共函数********************************

    /**
     * 根据菜单id删除角色菜单关联信息
     *
     * @param menuId 菜单id
     */
    @Transactional(readOnly = true)
    public void deleteByMenuId(Long menuId) {
        trRoleMenuMapper.delete(new LambdaQueryWrapper<TrRoleMenu>().eq(TrRoleMenu::getMenuId, menuId));
    }

    /**
     * 根据角色id集合批量删除角色菜单关联信息
     *
     * @param roleIdCollection 角色id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIdCollection(Collection<Long> roleIdCollection) {
        if (CollectionUtils.isEmpty(roleIdCollection)) {
            return;
        }
        trRoleMenuMapper.delete(new LambdaQueryWrapper<TrRoleMenu>()
                .in(TrRoleMenu::getRoleId, roleIdCollection));
    }

}
