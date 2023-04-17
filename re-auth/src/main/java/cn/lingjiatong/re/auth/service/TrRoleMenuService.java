package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.TrRoleMenuMapper;
import cn.lingjiatong.re.common.entity.TrRoleMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单关联模块service层
 *
 * @author Ling, Jiatong
 * Date: 2022/12/31 14:54
 */
@Slf4j
@Service
public class TrRoleMenuService {

    @Resource
    private TrRoleMenuMapper trRoleMenuMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************


    /**
     * 根据角色id列表获取菜单id列表
     *
     * @param roleIdList 角色id列表
     * @return 菜单id列表
     */
    @Transactional(readOnly = true)
    public List<Long> findMenuIdListByRoleIdList(List<Long> roleIdList) {
        return trRoleMenuMapper.selectList(new LambdaQueryWrapper<TrRoleMenu>()
                        .select(TrRoleMenu::getMenuId)
                .in(TrRoleMenu::getRoleId, roleIdList))
                .stream()
                .map(TrRoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
    }


}
