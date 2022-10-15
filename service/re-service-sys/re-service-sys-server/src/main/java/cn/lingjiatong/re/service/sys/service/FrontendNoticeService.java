package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import cn.lingjiatong.re.service.sys.constant.SysNoticeConstant;
import cn.lingjiatong.re.service.sys.constant.SysNoticeTypeEnum;
import cn.lingjiatong.re.service.sys.entity.SpToutiaoRb;
import cn.lingjiatong.re.service.sys.entity.SysNotice;
import cn.lingjiatong.re.service.sys.mapper.SpToutiaoRbMapper;
import cn.lingjiatong.re.service.sys.mapper.SysNoticeMapper;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.lingjiatong.re.service.sys.constant.SysNoticeConstant.NO_NOTICE_MESSAGE_DEFAULT_NEWS_ITEM_COUNT;

/**
 * 前端通知消息service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:21
 */
@Service
public class FrontendNoticeService {

    @Resource
    private SysNoticeMapper sysNoticeMapper;
    @Resource
    private SpToutiaoRbMapper spToutiaoRbMapper;

    // ********************************新增类接口********************************


    // ********************************删除类接口********************************

    // ********************************修改类接口********************************

    // ********************************查询类接口********************************

    /**
     * 获取前端通知消息列表
     *
     * @return 前端通知消息列表VO对象列表
     */
    public List<FrontendNoticeListVO> findFrontendNoticeList() {
        // 首先获取当前区段显示的消息列表
        String nowTimeStr = DateUtil.getNowString("yyyy-MM-dd HH:mm:ss");
        List<SysNotice> noticeByDateTime = sysNoticeMapper.findNoticeByDateTime(nowTimeStr);

        // 当消息不存在时，返回默认消息 + 10 条今天的热榜消息
        if (CollectionUtils.isEmpty(noticeByDateTime)) {
            FrontendNoticeListVO notNoticeMessageVO = new FrontendNoticeListVO();
            notNoticeMessageVO.setTitle(SysNoticeConstant.NO_NOTICE_MESSAGE);
            notNoticeMessageVO.setType(SysNoticeTypeEnum.NORMAL_NOTICE.getCode());

            List<SpToutiaoRb> spToutiaoRbList = spToutiaoRbMapper.selectList(new LambdaQueryWrapper<SpToutiaoRb>()
                    .select(SpToutiaoRb::getTitle, SpToutiaoRb::getLink, SpToutiaoRb::getState, SpToutiaoRb::getCreateTime)
                    .orderByDesc(SpToutiaoRb::getHotValue)
                    .last("LIMIT " + NO_NOTICE_MESSAGE_DEFAULT_NEWS_ITEM_COUNT)
            );

            // 将这些新闻消息插入sys_notice表，
            List<FrontendNoticeListVO> resultVOList =  Lists.newArrayList();
            spToutiaoRbList.forEach(rb -> {
                FrontendNoticeListVO v = new FrontendNoticeListVO();
                SysNotice sysNotice = new SysNotice();
                v.setTitle(rb.getTitle());
                v.setLink(rb.getLink());
                v.setNewsDate(LocalDate.from(rb.getCreateTime()));
                v.setNewsState(rb.getState());
                resultVOList.add(v);

                sysNotice.setLink(rb.getLink());
                sysNotice.setTitle(rb.getTitle());
                sysNotice.setType(SysNoticeTypeEnum.NEWS_NOTICE.getCode());
                // 设置为当前时间
                sysNotice.setStartTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                // 默认展示3天
                sysNotice.setEndTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).plusDays(3));
                sysNotice.setNewsDate(LocalDate.from(rb.getCreateTime()));
                sysNotice.setNewsState(rb.getState());
                sysNotice.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                sysNotice.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                // 这里理论上不会出现重复插入数据，所以不捕获异常
                sysNoticeMapper.insert(sysNotice);
            });

            resultVOList.add(notNoticeMessageVO);
            return resultVOList;
        }

        List<FrontendNoticeListVO> result = Lists.newArrayList();
        noticeByDateTime.forEach(notice -> {
            // 消息已经按照重要程度排序了
            FrontendNoticeListVO frontendNoticeListVO = new FrontendNoticeListVO();
            frontendNoticeListVO.setLink(notice.getLink());
            frontendNoticeListVO.setType(notice.getType());
            frontendNoticeListVO.setTitle(notice.getTitle());
            if (SysNoticeTypeEnum.NEWS_NOTICE.getCode().equals(notice.getType())) {
                frontendNoticeListVO.setNewsDate(notice.getNewsDate());
                frontendNoticeListVO.setNewsState(notice.getNewsState());
            }
            result.add(frontendNoticeListVO);
        });

        return result;
    }

}
