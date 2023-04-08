package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.entity.UserLoginLog;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendUserListVO;
import cn.lingjiatong.re.service.sys.mapper.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台菜单相关接口service层
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 10:12 PM
 */
@Service
public class BackendMenuService {

    @Autowired
    private MenuMapper menuMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 分页获取菜单列表
     *
     * @param dto 后台获取菜单列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<BackendMenuListVO> findMenuList(BackendMenuListDTO dto, User currentUser) {
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<BackendMenuListVO> menuList = menuMapper.findMenuList(page, dto);
        long total = menuMapper.findMenuListTotal(dto);
        page.setTotal(total);

        // 查询每个用户最后一次登录的信息数据
        // 对于每一个父级菜单查询其子级菜单
        List<BackendMenuListVO> records = menuList.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            records.forEach(vo -> {
                Long parentId = Long.valueOf(vo.getParentId());
                Long id = Long.valueOf(vo.getId());
                if (parentId.equals(-1L)) {
                    List<BackendMenuListVO> children = vo.getChildren();
                    if (CollectionUtils.isEmpty(children)) {
                        children = Lists.newArrayList();
                        vo.setChildren(children);
                    }
                    List<Menu> childMenuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                            .eq(Menu::getParentId, id));
                    if (!CollectionUtils.isEmpty(childMenuList)) {
                        for (Menu childMenu : childMenuList) {
                            BackendMenuListVO childVO = new BackendMenuListVO();
                            BeanUtils.copyProperties(childMenu, childVO);
                            childVO.setId(String.valueOf(childMenu.getId()));
                            childVO.setParentId(String.valueOf(childMenu.getParentId()));
                            children.add(childVO);
                        }
                    }
                }
            });
        }
        return menuList;
    }

    /**
     * 根据菜单id列表获取菜单列表
     *
     * @param menuIdList 菜单id列表
     * @return 菜单实体列表
     */
    public List<Menu> findMenuListByIdList(List<Long> menuIdList) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return Lists.newArrayList();
        }
        return menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, menuIdList));
    }

    /**
     * 根据菜单id列表校验菜单是否都存在
     *
     * @param menuIdCollection 菜单id集合
     * @return 都存在则返回true，否则返回false，参数为空返回false
     */
    public boolean isExistsByIdList(Collection<Long> menuIdCollection) {
        if (CollectionUtils.isEmpty(menuIdCollection)) {
            return false;
        }
        Integer count = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, menuIdCollection));
        return count.equals(menuIdCollection.size());
    }


    /**
     * 后台获取菜单树
     *
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单树VO对象列表
     */
    public List<BackendMenuTreeVO> findBackendMenuTree(User currentUser) {
        // 先查询后台管理系统所有的菜单列表
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getTitle)
                .eq(Menu::getProjectName, CommonConstant.PROJECT_NAME_BACKEND_PAGE));

        List<BackendMenuTreeVO> backendMenuTreeVOList = menuList
                .stream()
                .map(menu -> {
                    BackendMenuTreeVO vo = new BackendMenuTreeVO();
                    vo.setMenuName(menu.getName());
                    vo.setMenuTitle(menu.getTitle());
                    vo.setMenuId(menu.getId());
                    vo.setParentMenuId(menu.getParentId());
                    return vo;
                }).collect(Collectors.toList());

        return menuListToMenuTree(backendMenuTreeVOList, -1L);
    }

    // ********************************私有函数********************************

    /**
     * 将角色菜单权限列表转换为角色菜单权限树对象列表
     *
     * @param menuList 菜单实体列表
     * @return 角色菜单权限树对象列表
     */
    private List<BackendMenuTreeVO> menuListToMenuTree(List<BackendMenuTreeVO> menuList, Long parentId) {
        List<BackendMenuTreeVO> result = menuList
                .stream()
                // 过滤父节点
                .filter(parent -> parent.getParentMenuId().equals(parentId))
                .map(child -> {
                    child.setChildren(menuListToMenuTree(menuList, child.getMenuId()));
                    return child;
                }).collect(Collectors.toList());
        return result;
    }

    // ********************************公用函数********************************

}
