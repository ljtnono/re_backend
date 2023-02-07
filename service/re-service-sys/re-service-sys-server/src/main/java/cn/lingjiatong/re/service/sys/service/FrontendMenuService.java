package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.service.sys.api.vo.FrontendMenuVO;
import cn.lingjiatong.re.service.sys.entity.Menu;
import cn.lingjiatong.re.service.sys.mapper.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.lingjiatong.re.common.constant.CommonConstant.PROJECT_NAME_FRONTEND_PAGE;

/**
 * 前端菜单模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/2/7 09:46
 */
@Slf4j
@Service
public class FrontendMenuService {

    @Autowired
    private MenuMapper menuMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************

    /**
     * 获取前端菜单
     *
     * @return 前端菜单VO对象
     */
    public List<FrontendMenuVO> findFrontendMenu() {
        // 获取前端项目的所有菜单
        List<FrontendMenuVO> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getProjectName, PROJECT_NAME_FRONTEND_PAGE))
                .stream()
                .map(menu -> {
                    FrontendMenuVO frontendMenuVO = new FrontendMenuVO();
                    BeanUtils.copyProperties(menu, frontendMenuVO);
                    return frontendMenuVO;
                })
                .collect(Collectors.toList());

        Map<Long, List<FrontendMenuVO>> collect = menus.stream().filter(menu -> !menu.getParentId().equals(-1L)).collect(Collectors.groupingBy(FrontendMenuVO::getParentId));
        menus.forEach(menu -> menu.setChildren(collect.get(menu.getId())));
        menus = menus.stream().filter(menu -> menu.getParentId().equals(-1L)).collect(Collectors.toList());
        return menus;
    }



    // ********************************私有函数********************************
    // ********************************公用函数********************************

}
