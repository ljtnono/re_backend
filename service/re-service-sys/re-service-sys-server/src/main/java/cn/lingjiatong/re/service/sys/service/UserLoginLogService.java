package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.entity.UserLoginLog;
import cn.lingjiatong.re.service.sys.mapper.UserLoginLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 用户登录日志模块service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/15 16:09
 */
@Slf4j
@Service
public class UserLoginLogService {

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************
    // ********************************查询类接口********************************
    // ********************************私有函数********************************
    // ********************************公用函数********************************

    /**
     * 根据用户id列表获取用户最后一次登录日志列表
     *
     * @param userIdList 用户id列表
     * @return 用户登录日志实体列表
     */
    @Transactional(readOnly = true)
    public List<UserLoginLog> findUserLastLoginLogListByUserIdList(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return userLoginLogMapper.findUserLastLoginLogListByUserIdList(userIdList);
    }
}
