package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.MenuMapper;
import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.Menu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 菜单模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 15:51
 */
@Slf4j
@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 根据id和项目名称获取菜单列表
     *
     * @param menuIdList 菜单id列表
     * @param projectName 项目名称
     * @return 菜单实体列表
     */
    public List<Menu> getMenuListByIdListAndProjectName(List<Long> menuIdList, String projectName) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return Lists.newArrayList();
        }
        if (!Arrays.asList(CommonConstant.PROJECT_NAME_BACKEND_PAGE, CommonConstant.PROJECT_NAME_FRONTEND_PAGE).contains(projectName)) {
            return Lists.newArrayList();
        }
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .in(Menu::getId, menuIdList)
                .eq(Menu::getProjectName, projectName));
        if (CollectionUtils.isEmpty(menus)) {
            return Lists.newArrayList();
        }
        return menus;
    }

}
