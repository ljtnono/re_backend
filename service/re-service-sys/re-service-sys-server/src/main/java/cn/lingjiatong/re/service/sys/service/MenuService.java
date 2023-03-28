package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.service.sys.mapper.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单模块service层
 *
 * @author Ling, Jiatong
 * Date: 3/28/23 10:50 PM
 */
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 根据菜单id获取菜单实体
     *
     * @param menuId 菜单id
     * @return 菜单实体
     */
    public Menu findById(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    /**
     * 根据父菜单id获取子菜单id列表
     *
     * @param parentId 父菜单id
     * @return 子菜单id列表
     */
    public List<Long> findChildrenMenuIdList(Long parentId) {
        List<Menu> childrenMenuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getParentId, parentId));
        if (CollectionUtils.isEmpty(childrenMenuList)) {
            return Lists.newArrayList();
        }
        return childrenMenuList.stream().map(Menu::getId).collect(Collectors.toList());
    }

}
