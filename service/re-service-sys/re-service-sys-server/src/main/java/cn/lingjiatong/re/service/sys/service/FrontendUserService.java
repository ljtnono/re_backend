package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.CommonConstant;
import cn.lingjiatong.re.common.entity.User;
import cn.lingjiatong.re.service.sys.api.vo.FrontendUserListVO;
import cn.lingjiatong.re.service.sys.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端用户模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/1/17 10:41
 */
@Slf4j
@Service
public class FrontendUserService {

    @Resource
    private UserMapper userMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 根据用户id列表获取用户列表
     *
     * @param userIdList 用户id列表
     * @param fields 要获取的字段列表
     * @return 前端获取用户列表VO对象列表
     */
    public List<FrontendUserListVO> findUserListByUserIdList(List<Long> userIdList, SFunction<User, ?>... fields) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return List.of();
        }
        List<User> users;
        if (fields == null || fields.length == 0) {
            users = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .in(User::getId, userIdList)
                    .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        } else {
            users = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .select(fields)
                    .in(User::getId, userIdList)
                    .eq(User::getDeleted, CommonConstant.ENTITY_NORMAL));
        }
        if (CollectionUtils.isEmpty(users)) {
            return List.of();
        }
        return users.stream()
                .map(user -> {
                    FrontendUserListVO vo = new FrontendUserListVO();
                    BeanUtils.copyProperties(user, vo);
                    vo.setId(String.valueOf(user.getId()));
                    return vo;
                }).collect(Collectors.toList());
    }

}
