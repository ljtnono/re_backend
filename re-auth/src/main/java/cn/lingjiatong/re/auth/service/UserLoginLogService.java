package cn.lingjiatong.re.auth.service;

import cn.lingjiatong.re.auth.mapper.UserLoginLogMapper;
import cn.lingjiatong.re.common.entity.UserLoginLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志service层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/15 15:34
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
     * 保存用户登录日志
     *
     * @param userLoginLog 用户登录日志实体
     */
    public void insert(UserLoginLog userLoginLog) {
        userLoginLogMapper.insert(userLoginLog);
    }

}
