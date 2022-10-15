package cn.lingjiatong.re.service.sys.mapper;

import cn.lingjiatong.re.service.sys.entity.SysNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前端通知消息mapper层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:22
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 根据时间获取此时间可以显示的通知消息
     * 默认按照重要程度排序 系统消息 > 普通消息 > 新闻
     * 默认获取前50条数据
     *
     * @param startTime 消息开始时间
     * @return 系统通知实体类列表
     */
    List<SysNotice> findNoticeByDateTime(@Param("startTime") String startTime);
}
