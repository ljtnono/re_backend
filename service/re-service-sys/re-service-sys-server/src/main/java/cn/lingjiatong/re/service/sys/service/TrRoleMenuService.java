package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.TrRoleMenu;
import cn.lingjiatong.re.service.sys.mapper.TrRoleMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        return trRoleMenuList.stream().map(TrRoleMenu::getMenuId).collect(Collectors.toList());
    }

}
