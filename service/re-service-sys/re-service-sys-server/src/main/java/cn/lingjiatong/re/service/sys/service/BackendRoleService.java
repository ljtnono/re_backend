package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RoleErrorMessageConstant;
import cn.lingjiatong.re.common.constant.RoleRegexConstant;
import cn.lingjiatong.re.common.entity.*;
import cn.lingjiatong.re.common.exception.*;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.dto.BackendRolePageListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendRoleSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import cn.lingjiatong.re.service.sys.mapper.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    @Autowired
    private MenuService menuService;

    // ********************************新增类接口********************************

    /**
     * 后台保存角色
     *
     * @param dto 后台保存角色DTO对象
     * @param currentUser 当前登陆用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(BackendRoleSaveDTO dto, User currentUser) {
        // 参数校验
        checkBackendSaveRole(dto);
        Role role = new Role();
        role.setId(snowflakeIdWorkerUtil.nextId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        role.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        role.setDeleted(CommonConstant.ENTITY_NORMAL);

        try {
            // 插入角色
            roleMapper.insert(role);
            // 插入角色菜单关联信息
            Set<Long> menuIdSet = dto.getMenuIdSet();
            List<TrRoleMenu> trRoleMenuList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(menuIdSet)) {
                menuIdSet.forEach(menuId -> {
                    TrRoleMenu trRoleMenu = new TrRoleMenu();
                    trRoleMenu.setId(snowflakeIdWorkerUtil.nextId());
                    trRoleMenu.setRoleId(role.getId());
                    trRoleMenu.setMenuId(menuId);
                    trRoleMenuList.add(trRoleMenu);
                    try {
                        trRoleMenuService.saveTrRoleMenu(trRoleMenu);
                    } catch (DuplicateKeyException e) {
                        log.warn("忽略角色重复菜单，角色id：{}，菜单id：{}", trRoleMenu.getRoleId(), trRoleMenu.getMenuId());
                    }
                });
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }


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


    /**
     * 校验后台保存角色DTO对象
     *
     * @param dto 后台保存角色DTO对象
     */
    private void checkBackendSaveRole(BackendRoleSaveDTO dto) {
        String name = dto.getName();
        String description = dto.getDescription();
        Set<Long> menuIdSet = dto.getMenuIdSet();
        // 判空校验
        if (!StringUtils.hasLength(name)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_SAVE_NAME_EMPTY_ERROR_MESSAGE);
        }
        // 格式校验
        if (StringUtils.hasLength(name) && !RoleRegexConstant.ROLE_SAVE_NAME_REGEX.matcher(name).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_SAVE_NAME_FORMAT_ERROR_MESSAGE);
        }
        // 描述校验
        if (StringUtils.hasLength(description) && description.length() > 200) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_SAVE_DESCRIPTION_LENGTH_ERROR_MESSAGE);
        }
        // 角色名重复校验
        if (isRoleExist(name)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROLE_NAME_EXIST_ERROR_MESSAGE);
        }
        if (!CollectionUtils.isEmpty(menuIdSet)) {
            if (!backendMenuService.isExistsByIdList(menuIdSet)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_SAVE_MENU_ID_NOT_EXIST_MESSAGE);
            }
            // 如果菜单是子菜单，那么需要确保菜单中含有其父菜单id, 如果菜单是父菜单，那么当这个菜单的子菜单不存在时，则需要删除该父菜单id
            Iterator<Long> iterator = menuIdSet.iterator();
            while (iterator.hasNext()) {
                Long menuId = iterator.next();
                Menu menu = menuService.findById(menuId);
                Long parentId = menu.getParentId();
                if (parentId.equals(-1L)) {
                    // 查询子菜单id列表
                    List<Long> childrenMenuId = menuService.findChildrenMenuIdList(menuId);
                    if (CollectionUtils.isEmpty(Sets.intersection(menuIdSet, Set.of(childrenMenuId)))) {
                        iterator.remove();
                    }
                } else {
                    if (!menuIdSet.contains(parentId)) {
                        menuIdSet.add(parentId);
                    }
                }
            }
        }
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


    /**
     * 校验角色是否存在
     *
     * @param roleName 角色名
     * @return 存在返回true，不存在返回false
     */
    @Transactional(readOnly = true)
    public boolean isRoleExist(String roleName) {
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getName)
                .eq(Role::getName, roleName));
        return role != null;
    }

}
