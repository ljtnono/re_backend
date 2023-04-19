package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.constant.MenuErrorMessageConstant;
import cn.lingjiatong.re.common.constant.MenuRegexConstant;
import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.entity.Menu;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.common.exception.*;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuListDTO;
import cn.lingjiatong.re.service.sys.api.dto.BackendMenuSaveDTO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuListVO;
import cn.lingjiatong.re.service.sys.api.vo.BackendMenuTreeVO;
import cn.lingjiatong.re.service.sys.mapper.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单相关接口service层
 *
 * @author Ling, Jiatong
 * Date: 3/25/23 10:12 PM
 */
@Slf4j
@Service
public class BackendMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private BackendRouteService backendRouteService;
    @Autowired
    private PermissionService permissionService;

    // ********************************新增类接口********************************

    /**
     * 新增菜单
     *
     * @param dto 后台新增菜单DTO对象
     * @param currentUser 当前登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(BackendMenuSaveDTO dto, User currentUser) {
        // 校验是是否是超级管理员
        Long userId = currentUser.getId();
        if (!UserConstant.SUPER_ADMIN_USER_ID.equals(userId)) {
            // 只允许lingjiatong账号修改菜单相关内容
            throw new PermissionException(ErrorEnum.NO_PERMISSION_ERROR);
        }
        // 校验新增菜单DTO对象
        checkBackendMenuSaveDTO(dto);

        try {
            // 新增菜单
            Menu menu = new Menu();
            menu.setId(generateNewMenuId(dto.getParentId()));
            menu.setParentId(dto.getParentId());
            menu.setName(dto.getName());
            menu.setIcon(dto.getIcon());
            if (!Long.valueOf(-1L).equals(dto.getParentId())) {
                menu.setPath(dto.getPath());
                menu.setComponentName(dto.getComponentName());
                menu.setComponentPath(dto.getComponentPath());
            }
            menu.setTitle(dto.getTitle());
            menu.setProjectName(CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            menuMapper.insert(menu);

            // 生成新菜单的路由
            backendRouteService.saveNewMenuRoute(menu, CommonConstant.PROJECT_NAME_BACKEND_PAGE);
            // TODO 自动生成新权限


        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServerException(ErrorEnum.DATABASE_OPERATION_ERROR);
        }
    }

    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    /**
     * 分页获取菜单列表
     *
     * @param dto 后台获取菜单列表DTO对象
     * @param currentUser 当前登陆用户
     * @return 后台获取菜单列表VO对象列表
     */
    @Transactional(readOnly = true)
    public List<BackendMenuListVO> findMenuList(BackendMenuListDTO dto, User currentUser) {
        List<BackendMenuListVO> menuList = menuMapper.findMenuList(dto);
        menuList.forEach(this::dfsGenerateMenuChildren);
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
     * 深度优先搜索生成菜单的子菜单
     *
     * @param menu 每次循环的菜单
     */
    private void dfsGenerateMenuChildren(BackendMenuListVO menu) {
        Long id = Long.valueOf(menu.getId());
        List<BackendMenuListVO> cc = menu.getChildren();
        if (CollectionUtils.isEmpty(cc)) {
            cc = Lists.newArrayList();
            menu.setChildren(cc);
        }
        // 查询子菜单
        List<Menu> childMenuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id));
        if (!CollectionUtils.isEmpty(childMenuList)) {
            for (Menu childMenu : childMenuList) {
                BackendMenuListVO childVO = new BackendMenuListVO();
                BeanUtils.copyProperties(childMenu, childVO);
                childVO.setId(String.valueOf(childMenu.getId()));
                childVO.setParentId(String.valueOf(childMenu.getParentId()));
                cc.add(childVO);
                dfsGenerateMenuChildren(childVO);
            }
        }
    }

    /**
     * 生成新的菜单id
     *
     * @param parentId 菜单的父级菜单id
     * @return 新的菜单id
     */
    private Long generateNewMenuId(Long parentId) {
        Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getParentId, parentId)
                .orderByDesc(Menu::getId)
                .last("LIMIT 1"));
        if (menu == null) {
            return 10000L;
        } else {
            if (Long.valueOf(-1L).equals(parentId)) {
                // 如果新增的是父级菜单，查询最大的父级菜单，然后+100
                return menu.getId() + 100;
            } else {
                // 如果新增的是子级菜单，查询该父级菜单下最大的子菜单id然后+1
                return menu.getId() + 1;
            }
        }
    }



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

    /**
     * 校验新增菜单DTO对象
     *
     * @param dto 新增菜单DTO对象
     */
    private void checkBackendMenuSaveDTO(BackendMenuSaveDTO dto) {
        Long parentId = dto.getParentId();
        String title = dto.getTitle();
        String name = dto.getName();
        String path = dto.getPath();
        String componentName = dto.getComponentName();
        String componentPath = dto.getComponentPath();
        List<BackendMenuSaveDTO.MenuPermission> permissionList = dto.getPermissionList();

        if (parentId == null) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.PARENT_ID_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(title)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_TITLE_EMPTY_ERROR_MESSAGE);
        }
        if (!StringUtils.hasLength(name)) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_NAME_EMPTY_ERROR_MESSAGE);
        }
        if (!Long.valueOf(-1L).equals(parentId)) {
            if (!StringUtils.hasLength(path)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PATH_EMPTY_ERROR_MESSAGE);
            }
            if (!StringUtils.hasLength(componentName)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_NAME_EMPTY_ERROR_MESSAGE);
            }
            if (!StringUtils.hasLength(componentPath)) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_PATH_EMPTY_ERROR_MESSAGE);
            }
        }

        // 正则校验
        if (!MenuRegexConstant.MENU_TITLE_REGEX.matcher(title).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_TITLE_FORMAT_ERROR_MESSAGE);
        }
        if (!MenuRegexConstant.MENU_NAME_REGEX.matcher(name).matches()) {
            throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_NAME_FORMAT_ERROR_MESSAGE);
        }
        if (!Long.valueOf(-1L).equals(parentId)) {
            if (!MenuRegexConstant.MENU_PATH_REGEX.matcher(path).matches()) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_PATH_FORMAT_ERROR_MESSAGE);
            }
            if (!MenuRegexConstant.MENU_COMPONENT_NAME.matcher(componentName).matches()) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_NAME_FORMAT_ERROR_MESSAGE);
            }
            if (!MenuRegexConstant.MENU_COMPONENT_PATH.matcher(componentPath).matches()) {
                throw new ParamErrorException(ErrorEnum.ILLEGAL_PARAM_ERROR.getCode(), MenuErrorMessageConstant.MENU_COMPONENT_PATH_FORMAT_ERROR_MESSAGE);
            }
        }

        // TODO 权限表达式校验

        // 业务校验，校验父菜单是否存在
        if (!Long.valueOf(-1L).equals(parentId)) {
            Menu menu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                    .select(Menu::getId)
                    .eq(Menu::getId, parentId));
            if (menu == null) {
                throw new ResourceNotExistException(ErrorEnum.RESOURCE_NOT_EXIST_ERROR.getCode(), MenuErrorMessageConstant.MENU_PARENT_MENU_NOT_EXIST_ERROR_MESSAGE);
            }
        }
    }


    // ********************************公用函数********************************

}
