package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.RoleConstant;
import cn.lingjiatong.re.common.constant.RoleErrorMessageConstant;
import cn.lingjiatong.re.common.constant.RoleRegexConstant;
import cn.lingjiatong.re.common.entity.*;
import cn.lingjiatong.re.common.exception.*;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.dto.*;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendRoleMenuTreeVO;
import cn.lingjiatong.re.service.sys.mapper.RoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import java.util.*;
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
    @Autowired
    private TrUserRoleService trUserRoleService;
    @Autowired
    private TrRolePermissionService trRolePermissionService;

    // ********************************新增类接口********************************

    /**
     * 后台保存角色
     *
     * @param dto         后台保存角色DTO对象
     * @param currentUser 当前登陆用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(BackendRoleSaveDTO dto, User currentUser) {
        // 参数校验
        checkBackendRoleSaveDTO(dto);
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
            if (!CollectionUtils.isEmpty(menuIdSet)) {
                menuIdSet.forEach(menuId -> {
                    TrRoleMenu trRoleMenu = new TrRoleMenu();
                    trRoleMenu.setId(snowflakeIdWorkerUtil.nextId());
                    trRoleMenu.setRoleId(role.getId());
                    trRoleMenu.setMenuId(menuId);
                    try {
                        // 新增角色菜单关联关系
                        trRoleMenuService.saveTrRoleMenu(trRoleMenu);
                    } catch (DuplicateKeyException e) {
                        log.warn("忽略角色重复菜单，角色id：{}，菜单id：{}", trRoleMenu.getRoleId(), trRoleMenu.getMenuId());
                    }
                });
                // 查询菜单对应的所有权限id列表
                List<Long> permissionIdList = permissionService
                        .findPermissionIdListByMenuIdCollectionAndProjectName(menuIdSet, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
                if (!CollectionUtils.isEmpty(permissionIdList)) {
                    List<TrRolePermission> rolePermissionList = permissionIdList
                            .stream().map(permissionId -> {
                                TrRolePermission trp = new TrRolePermission();
                                trp.setId(snowflakeIdWorkerUtil.nextId());
                                trp.setRoleId(role.getId());
                                trp.setPermissionId(permissionId);
                                return trp;
                            }).collect(Collectors.toList());
                    // 插入角色权限关联表数据
                    trRolePermissionService.saveTrRolePermissionBatch(rolePermissionList);
                }
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }


    // ********************************删除类接口********************************

    /**
     * 后台批量删除角色
     *
     * @param dto         后台角色批量删除DTO对象
     * @param currentUser 当前登陆用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleBatch(BackendRoleDeleteBatchDTO dto, User currentUser) {
        Set<Long> roleIdSet = dto.getRoleIdSet();
        if (CollectionUtils.isEmpty(roleIdSet)) {
            return;
        }
        // 校验角色是否都存在
        if (!isRoleExist(roleIdSet)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR);
        }
        // 超级管理员角色不能删除
        if (roleIdSet.contains(RoleConstant.SUPER_ADMIN_ROLE_ID)) {
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR.getCode(), RoleErrorMessageConstant.CAN_NOT_DELETE_ADMIN_ROLE_ERROR_MESSAGE);
        }
        // 删除角色必须得该角色不存在关联的用户才能删除
        roleIdSet.forEach(roleId -> {
            List<Long> userIdList = trUserRoleService.findUserIdListByRoleId(roleId);
            if (!CollectionUtils.isEmpty(userIdList)) {
                throw new BusinessException(ErrorEnum.ROLE_IS_RELATED_BY_USER_ERROR_MESSAGE);
            }
        });

        try {
            // 删除角色
            roleMapper.deleteBatchIds(roleIdSet);
            // 删除角色菜单关联
            trRoleMenuService.deleteByRoleIdCollection(roleIdSet);
            // 删除角色权限关联
            trRolePermissionService.deleteByRoleIdCollection(roleIdSet);
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }


    // ********************************修改类接口********************************

    /**
     * 后台更新角色
     *
     * @param dto 后台更新角色DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(BackendRoleUpdateDTO dto, User currentUser) {
        // 校验后台更新角色DTO对象
        checkBackendRoleUpdateDTO(dto);
        // 无法修改超级管理员角色
        if (RoleConstant.SUPER_ADMIN_ROLE_ID.equals(dto.getRoleId())) {
            throw new PermissionException(ErrorEnum.PERMISSION_DENIED_ERROR.getCode(), RoleErrorMessageConstant.CAN_NOT_UPDATE_ADMIN_ROLE_ERROR_MESSAGE);
        }
        try {
            // 更新角色信息
            roleMapper.update(null, new LambdaUpdateWrapper<Role>()
                    .set(Role::getName, dto.getName())
                    .set(Role::getDescription, dto.getDescription())
                    .set(Role::getModifyTime, LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                    .eq(Role::getId, dto.getRoleId()));
            // 更新角色关联菜单信息，先删除原来的菜单关联信息
            trRoleMenuService.deleteByRoleIdCollection(List.of(dto.getRoleId()));
            Set<Long> menuIdSet = dto.getMenuIdSet();
            if (!CollectionUtils.isEmpty(menuIdSet)) {
                menuIdSet.forEach(menuId -> {
                    TrRoleMenu trRoleMenu = new TrRoleMenu();
                    trRoleMenu.setRoleId(dto.getRoleId());
                    trRoleMenu.setMenuId(menuId);
                    trRoleMenu.setId(snowflakeIdWorkerUtil.nextId());
                    trRoleMenuService.saveTrRoleMenu(trRoleMenu);
                });
            }
            // 更新角色权限关联信息，先删除，再插入
            trRolePermissionService.deleteByRoleIdCollection(Set.of(dto.getRoleId()));
            List<Long> permissionIdList = permissionService.findPermissionIdListByMenuIdCollectionAndProjectName(menuIdSet, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            if (!CollectionUtils.isEmpty(permissionIdList)) {
                List<TrRolePermission> trRolePermissionList = permissionIdList
                        .stream()
                        .map(permissionId -> {
                            TrRolePermission trp = new TrRolePermission();
                            trp.setRoleId(dto.getRoleId());
                            trp.setPermissionId(permissionId);
                            trp.setId(snowflakeIdWorkerUtil.nextId());
                            return trp;
                        }).collect(Collectors.toList());
                trRolePermissionService.saveTrRolePermissionBatch(trRolePermissionList);
            }

            // TODO 考虑是否要强制下线相关的用户，不然可能存在用户能够访问没有权限的菜单的情况
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }



    // ********************************查询类接口********************************

    /**
     * 新增角色时对角色名称进行校验
     *
     * @param roleName    角色名称
     * @param currentUser 当前登陆用户
     * @return 可用返回true，不可用返回false
     */
    @Transactional(readOnly = true)
    public boolean addFormRoleNameCheck(String roleName, User currentUser) {
        // 判空校验
        if (!StringUtils.hasLength(roleName)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_EMPTY_ERROR_MESSAGE);
        }
        // 格式校验
        if (StringUtils.hasLength(roleName) && !RoleRegexConstant.ROLE_SAVE_NAME_REGEX.matcher(roleName).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_FORMAT_ERROR_MESSAGE);
        }

        return !isRoleExist(roleName);
    }

    /**
     * 编辑角色表单角色名称校验
     *
     * @param dto 后台校验角色名称DTO对象
     * @param currentUser 当前登录用户
     * @return 可用返回true，不可用返回false
     */
    @Transactional(readOnly = true)
    public boolean editFormRoleNameCheck(BackendRoleNameCheckDTO dto, User currentUser) {
        Optional.ofNullable(dto)
                .orElseThrow(() -> new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR));
        String roleName = dto.getRoleName();
        Long roleId = Long.valueOf(dto.getRoleId());
        if (roleId == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_UPDATE_ID_EMPTY_ERROR_MESSAGE);
        }
        if (!isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NOT_EXIST_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(roleName)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_EMPTY_ERROR_MESSAGE);
        }
        // 格式校验
        if (StringUtils.hasLength(roleName) && !RoleRegexConstant.ROLE_SAVE_NAME_REGEX.matcher(roleName).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_FORMAT_ERROR_MESSAGE);
        }
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getName, roleName)
                .ne(Role::getId, roleId));
        return count.equals(0);
    }

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
                    vo.setId(String.valueOf(role.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 后台分页获取角色列表
     *
     * @param dto         后台分页获取角色列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取角色列表VO对象分页对象
     */
    @Transactional(readOnly = true)
    public Page<BackendRoleListVO> findRolePageList(BackendRolePageListDTO dto, User currentUser) {
        dto.generateOrderCondition();
        Page page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 不查询总数
        page.setSearchCount(false);
        Page<BackendRoleListVO> rolePageList = roleMapper
                .findRolePageList(new Page<>(dto.getPageNum(), dto.getPageSize()), dto);
        long total = roleMapper.findRolePageListTotal(dto);
        page.setTotal(total);

        rolePageList.getRecords().forEach(vo -> {
            // 获取每个角色的菜单树
            Long roleId = Long.valueOf(vo.getId());
            BackendRoleMenuTreeVO backendRoleMenuTreeVO = new BackendRoleMenuTreeVO();
            backendRoleMenuTreeVO.setRoleId(roleId);

            // 获取角色的所有菜单id列表
            List<Long> menuIdList = trRoleMenuService.findMenuIdListByRoleId(roleId);
            if (CollectionUtils.isEmpty(menuIdList)) {
                backendRoleMenuTreeVO.setMenuTree(Lists.newArrayList());
                vo.setRoleMenuTree(backendRoleMenuTreeVO);
                return;
            }
            List<Menu> menuList = backendMenuService.findMenuListByIdList(menuIdList);
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
            backendRoleMenuTreeVO.setMenuTree(menuTreeList);
            vo.setRoleMenuTree(backendRoleMenuTreeVO);
        });
        return rolePageList;
    }

    /**
     * 获取角色的菜单树
     *
     * @param roleId      角色id
     * @param currentUser 当前登陆用户
     * @return 角色菜单树VO对象
     */
    @Transactional(readOnly = true)
    public BackendRoleMenuTreeVO findRoleMenuTree(Long roleId, User currentUser) {
        BackendRoleMenuTreeVO backendRoleMenuTreeVO = new BackendRoleMenuTreeVO();
        backendRoleMenuTreeVO.setRoleId(roleId);
        // 判断角色是否存在
        if (!isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR);
        }
        // 获取角色的所有菜单id列表
        List<Long> menuIdList = trRoleMenuService.findMenuIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(menuIdList)) {
            backendRoleMenuTreeVO.setMenuTree(Lists.newArrayList());
            return backendRoleMenuTreeVO;
        }
        List<Menu> menuList = backendMenuService.findMenuListByIdList(menuIdList);

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
        backendRoleMenuTreeVO.setMenuTree(menuTreeList);
        return backendRoleMenuTreeVO;
    }

    // ********************************私有函数********************************

    /**
     * 校验后台更新角色
     *
     * @param dto 后台更新角色DTO对象
     */
    private void checkBackendRoleUpdateDTO(BackendRoleUpdateDTO dto) {
        Long roleId = dto.getRoleId();
        String name = dto.getName();
        String description = dto.getDescription();
        Set<Long> menuIdSet = dto.getMenuIdSet();

        // 判空校验
        if (roleId == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_UPDATE_ID_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(name)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_EMPTY_ERROR_MESSAGE);
        }
        // 格式校验
        if (StringUtils.hasLength(name) && !RoleRegexConstant.ROLE_SAVE_NAME_REGEX.matcher(name).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_FORMAT_ERROR_MESSAGE);
        }
        // 描述校验
        if (StringUtils.hasLength(description) && description.length() > 200) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE);
        }
        // 校验角色是否存在
        if (!isRoleExist(roleId)) {
            throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NOT_EXIST_ERROR_MESSAGE);
        }
        // 校验角色名是否重复
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getName, name)
                .ne(Role::getId, roleId));
        if (count > 0) {
            throw new BusinessException(ErrorEnum.NAME_OCCUPY_BY_OTHER_ROLE_ERROR_MESSAGE);
        }

        // 校验菜单id是否正确
        if (!CollectionUtils.isEmpty(menuIdSet)) {
            if (!backendMenuService.isExistsByIdList(menuIdSet)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_MENU_ID_NOT_EXIST_ERROR_MESSAGE);
            }
            // 如果菜单是子菜单，那么需要确保菜单中含有其父菜单id, 如果菜单是父菜单，那么当这个菜单的子菜单不存在时，则需要删除该父菜单id
            Iterator<Long> iterator = menuIdSet.iterator();
            while (iterator.hasNext()) {
                Long menuId = iterator.next();
                Menu menu = menuService.findById(menuId);
                Long parentId = menu.getParentId();
                if (parentId.equals(-1L)) {
                    // 查询子菜单id列表
                    List<Long> childrenMenuIdList = menuService.findChildrenMenuIdList(menuId);
                    Set<Long> childrenMenuIdSet = new HashSet<>(childrenMenuIdList);
                    if (CollectionUtils.isEmpty(Sets.intersection(menuIdSet, childrenMenuIdSet))) {
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
    private void checkBackendRoleSaveDTO(BackendRoleSaveDTO dto) {
        String name = dto.getName();
        String description = dto.getDescription();
        Set<Long> menuIdSet = dto.getMenuIdSet();
        // 判空校验
        if (!StringUtils.hasLength(name)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_EMPTY_ERROR_MESSAGE);
        }
        // 格式校验
        if (StringUtils.hasLength(name) && !RoleRegexConstant.ROLE_SAVE_NAME_REGEX.matcher(name).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_NAME_FORMAT_ERROR_MESSAGE);
        }
        // 描述校验
        if (StringUtils.hasLength(description) && description.length() > 200) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE);
        }
        // 角色名重复校验
        if (isRoleExist(name)) {
            throw new ResourceAlreadyExistException(ErrorEnum.ROLE_NAME_EXIST_ERROR_MESSAGE);
        }
        if (!CollectionUtils.isEmpty(menuIdSet)) {
            if (!backendMenuService.isExistsByIdList(menuIdSet)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), RoleErrorMessageConstant.ROLE_MENU_ID_NOT_EXIST_ERROR_MESSAGE);
            }
            // 如果菜单是子菜单，那么需要确保菜单中含有其父菜单id, 如果菜单是父菜单，那么当这个菜单的子菜单不存在时，则需要删除该父菜单id
            Iterator<Long> iterator = menuIdSet.iterator();
            while (iterator.hasNext()) {
                Long menuId = iterator.next();
                Menu menu = menuService.findById(menuId);
                Long parentId = menu.getParentId();
                if (parentId.equals(-1L)) {
                    // 查询子菜单id列表
                    List<Long> childrenMenuIdList = menuService.findChildrenMenuIdList(menuId);
                    Set<Long> childrenMenuIdSet = new HashSet<>(childrenMenuIdList);
                    // 这里使用Sets.of函数将childrenMenuIdList转换为set会导致求交集产生错误
                    if (CollectionUtils.isEmpty(Sets.intersection(menuIdSet, childrenMenuIdSet))) {
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
     * 校验角色是否都存在
     *
     * @param roleIdCollection 角色id集合
     * @return 都存在则返回true, 否则返回false
     */
    @Transactional(readOnly = true)
    public boolean isRoleExist(Collection<Long> roleIdCollection) {
        if (CollectionUtils.isEmpty(roleIdCollection)) {
            return false;
        }
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .in(Role::getId, roleIdCollection));
        return count.equals(roleIdCollection.size());
    }

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
