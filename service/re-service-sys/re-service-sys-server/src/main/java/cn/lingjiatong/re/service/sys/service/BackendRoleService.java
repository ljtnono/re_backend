package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.Permission;
import cn.lingjiatong.re.common.entity.Role;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import cn.lingjiatong.re.common.exception.ResourceNotExistException;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import cn.lingjiatong.re.service.sys.mapper.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台角色模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/3 14:34
 */
@Slf4j
@Service
public class BackendRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TrRoleMenuService trRoleMenuService;
    @Autowired
    private BackendMenuService backendMenuService;
    @Autowired
    private PermissionService permissionService;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取角色列表
     *
     * @param currentUser 当前用户
     * @return 后端角色列表VO对象列表
     */
    @Transactional(readOnly = true)
    public List<BackendRoleListVO> findRoleList(User currentUser) {
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, CommonConstant.ENTITY_NORMAL));
        return roleList
                .stream()
                .map(role -> {
                    BackendRoleListVO vo = new BackendRoleListVO();
                    BeanUtils.copyProperties(role, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 后台分页获取角色列表
     *
     * @param dto 后台分页获取角色列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取角色列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<BackendRoleListVO> findRolePageList(BackendRolePageListDTO dto, User currentUser) {
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<BackendRoleListVO> rolePageList = roleMapper.findRolePageList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
        long total = roleMapper.findRolePageListTotal(dto);
        page.setTotal(total);
        return rolePageList;
    }

    /**
     * 获取角色的菜单树
     *
     * @param roleId 角色id
     * @param currentUser 当前登陆用户
     * @return 角色菜单树VO对象
     */
    @Transactional(readOnly = true)
    public BackendRoleMenuTreeVO findRoleMenuTree(Long roleId, User currentUser) {
        BackendRoleMenuTreeVO backendRoleMenuTreeVO = new BackendRoleMenuTreeVO();
        // 判断角色是否存在
        if (!isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR);
        }
        backendRoleMenuTreeVO.setRoleId(roleId);
        // 获取角色的所有菜单id列表
        List<Long> menuIdList = trRoleMenuService.findMenuIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(menuIdList)) {
            backendRoleMenuTreeVO.setMenuTree(Lists.newArrayList());
            return backendRoleMenuTreeVO;
        }
        List<Menu> menuList = backendMenuService.findMenuListByIdList(menuIdList);
        if (CollectionUtils.isEmpty(menuList)) {
            backendRoleMenuTreeVO.setMenuTree(Lists.newArrayList());
            return backendRoleMenuTreeVO;
        }

        List<BackendRoleMenuTreeVO.MenuTree> roleMenuList = menuList
                .stream()
                .map(menu -> {
                    BackendRoleMenuTreeVO.MenuTree menuTree = new BackendRoleMenuTreeVO.MenuTree();
                    menuTree.setMenuName(menu.getName());
                    menuTree.setMenuId(menu.getId());
                    menuTree.setParentMenuId(menu.getParentId());
                    menuTree.setMenuTitle(menu.getTitle());
                    return menuTree;
                })
                .collect(Collectors.toList());
        List<BackendRoleMenuTreeVO.MenuTree> menuTreeList = menuListToMenuTree(roleMenuList, -1L);
        // 遍历菜单树，根据菜单树的menuId去查询菜单的权限列表，这里只有两层菜单，可以直接遍历
        for (BackendRoleMenuTreeVO.MenuTree menuTree : menuTreeList) {
            List<BackendRoleMenuTreeVO.MenuTree> children = menuTree.getChildren();
            for (BackendRoleMenuTreeVO.MenuTree child : children) {
                Long menuId = child.getMenuId();
                List<Permission> permissionList = permissionService
                        .findPermissionListByMenuIdAndProjectName(menuId, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
                if (CollectionUtils.isEmpty(permissionList)) {
                    child.setPermissionList(Lists.newArrayList());
                }
                List<BackendRoleMenuTreeVO.MenuPermission> menuPermissionList = Lists.newArrayList();
                permissionList.forEach(p -> {
                    BackendRoleMenuTreeVO.MenuPermission menuPermission = new BackendRoleMenuTreeVO.MenuPermission();
                    menuPermission.setExpression(p.getExpression());
                    menuPermission.setPermissionId(p.getId());
                    menuPermission.setPermissionName(p.getName());
                    menuPermissionList.add(menuPermission);
                });
                child.setPermissionList(menuPermissionList);
            }
        }

        backendRoleMenuTreeVO.setMenuTree(menuTreeList);
        return backendRoleMenuTreeVO;
    }

    // ********************************私有函数********************************

    /**
     * 将角色菜单权限列表转换为角色菜单权限树对象列表
     *
     * @param menuList 菜单实体列表
     * @return 角色菜单权限树对象列表
     */
    private List<BackendRoleMenuTreeVO.MenuTree> menuListToMenuTree(List<BackendRoleMenuTreeVO.MenuTree> menuList, Long parentId) {
        List<BackendRoleMenuTreeVO.MenuTree> result = menuList
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

    /**
     * 校验角色是否存在
     *
     * @param roleId 角色id
     * @return 存在返回true，不存在返回false
     */
    @Transactional(readOnly = true)
    public boolean isRoleExist(Long roleId) {
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getId, roleId));
        return role != null;
    }


}
