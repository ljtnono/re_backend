package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.Route;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.BackendRouteListVO;
import cn.lingjiatong.re.service.sys.mapper.RouteMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台路由模块service层
 *
 * @author Ling, Jiatong
 * Date: 4/10/23 10:42 PM
 */
@Service
public class BackendRouteService {

    @Autowired
    private RouteMapper routeMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************


    /**
     * 获取前端路由列表
     *
     * @param projectName 所属项目名称
     * @return 前端路由列表
     */
    @Transactional(readOnly = true)
    public List<BackendRouteListVO> findBackendRouteList(String projectName) {
        return routeMapper.selectList(new LambdaQueryWrapper<Route>()
                .eq(Route::getProjectName, projectName))
                .stream()
                .map(route -> {
                    BackendRouteListVO vo = new BackendRouteListVO();
                    BeanUtils.copyProperties(route, vo);
                    return vo;
                }).collect(Collectors.toList());
    }


}
