package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.constant.UserConstant;
import cn.lingjiatong.re.common.util.DateUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.service.sys.api.vo.FrontendNoticeListVO;
import cn.lingjiatong.re.service.sys.constant.SysNoticeConstant;
import cn.lingjiatong.re.service.sys.constant.SysNoticeTypeEnum;
import cn.lingjiatong.re.service.sys.entity.SpToutiaoRb;
import cn.lingjiatong.re.service.sys.entity.SysNotice;
import cn.lingjiatong.re.service.sys.mapper.SpToutiaoRbMapper;
import cn.lingjiatong.re.service.sys.mapper.SysNoticeMapper;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static cn.lingjiatong.re.service.sys.constant.SysNoticeConstant.NO_NOTICE_MESSAGE_DEFAULT_NEWS_ITEM_COUNT;

/**
 * 前端通知消息service层
 *
 * @author Ling, Jiatong
 * Date: 2022/10/15 14:21
 */
@Slf4j
@Service
public class FrontendNoticeService {

    @Resource
    private SysNoticeMapper sysNoticeMapper;
    @Resource
    private SpToutiaoRbMapper spToutiaoRbMapper;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    @Autowired
    @Qualifier("commonThreadPool")
    private ExecutorService commonThreadPool;
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
        List<FrontendNoticeListVO> result = Lists.newArrayList();

        // 当消息不存在时，返回 10 条今天的热榜消息
        FrontendNoticeListVO noNoticeMessageVO = new FrontendNoticeListVO();
        noNoticeMessageVO.setTitle(SysNoticeConstant.NO_NOTICE_MESSAGE);
        noNoticeMessageVO.setType(SysNoticeTypeEnum.NORMAL_NOTICE.getCode());
        if (CollectionUtils.isEmpty(noticeByDateTime) || noticeByDateTime.size() < NO_NOTICE_MESSAGE_DEFAULT_NEWS_ITEM_COUNT) {

            List<SpToutiaoRb> spToutiaoRbList = spToutiaoRbMapper.selectList(new LambdaQueryWrapper<SpToutiaoRb>()
                    .select(SpToutiaoRb::getTitle, SpToutiaoRb::getLink, SpToutiaoRb::getState, SpToutiaoRb::getCreateTime)
                    .orderByDesc(SpToutiaoRb::getCreateTime)
                    .last("LIMIT " + NO_NOTICE_MESSAGE_DEFAULT_NEWS_ITEM_COUNT)
            );


            List<SysNotice> noticeToInsertList = Lists.newArrayList();
            spToutiaoRbList.parallelStream().forEach(rb -> {
                FrontendNoticeListVO v = new FrontendNoticeListVO();
                SysNotice sysNotice = new SysNotice();
                v.setTitle(rb.getTitle());
                v.setLink(rb.getLink());
                v.setNewsDate(LocalDate.from(rb.getCreateTime()));
                v.setNewsState(rb.getState());
                result.add(v);

                sysNotice.setId(snowflakeIdWorkerUtil.nextId());
                sysNotice.setLink(rb.getLink());
                sysNotice.setTitle(rb.getTitle());
                sysNotice.setType(SysNoticeTypeEnum.NEWS_NOTICE.getCode());
                // 设置为当前时间
                sysNotice.setStartTime(DateUtil.getLocalDateTimeNow());
                // 默认展示3天
                sysNotice.setEndTime(DateUtil.getLocalDateTimeNow().plusDays(3));
                sysNotice.setNewsDate(LocalDate.from(rb.getCreateTime()));
                sysNotice.setNewsState(rb.getState());
                sysNotice.setCreateTime(DateUtil.getLocalDateTimeNow());
                sysNotice.setModifyTime(DateUtil.getLocalDateTimeNow());
                // 默认为系统操作用户
                sysNotice.setOptUser(UserConstant.SYSTEM_USER);
                noticeToInsertList.add(sysNotice);
            });
            commonThreadPool.execute(() -> {
                try {
                    Log mybatisLog = LogFactory.getLog(this.getClass());
                    String sqlStatement = SqlHelper.getSqlStatement(SysNoticeMapper.class, SqlMethod.INSERT_ONE);
                    SqlHelper.executeBatch(SysNotice.class, mybatisLog, noticeToInsertList, 500, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
                } catch (DuplicateKeyException e) {
                    log.warn("==========重复的通知，不插入");
                } catch (Throwable e) {
                    log.error(e.toString(), e);
                }
            });
            return result;
        }

        noticeByDateTime.parallelStream().forEach(notice -> {
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

        result.add(noNoticeMessageVO);
        return result;
    }

}
