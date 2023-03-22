package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.common.entity.UserLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户登录日志模块mapper层
 *
 * @author Ling, Jiatong
 * Date: 2023/3/15 15:00
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {


    // ********************************新增类接口********************************
    // ********************************删除类接口********************************
    // ********************************修改类接口********************************

    /**
     * 定期删除用户登陆日志
     */
    void deleteUserLoginLogSchedule();

    // ********************************查询类接口********************************

    /**
     * 根据用户id列表获取用户最后一次登录日志列表
     *
     * @param userIdList 用户id列表
     * @return 用户登录日志实体列表
     */
    List<UserLoginLog> findUserLastLoginLogListByUserIdList(@Param("userIdList") List<Long> userIdList);


    // ********************************私有函数********************************
    // ********************************公用函数********************************

}
