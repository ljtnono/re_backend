package cn.lingjiatong.re.job.crawler;

import cn.lingjiatong.re.common.util.EncryptUtil;
import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.job.bo.ToutiaoHotBO;
import cn.lingjiatong.re.job.entity.SpToutiaoRb;
import cn.lingjiatong.re.job.enumeration.ToutiaoRbEnum;
import cn.lingjiatong.re.job.mapper.SpToutiaoRbMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 今日头条热榜爬虫
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 01:57
 */
@Slf4j
@Component
public class ToutiaoHotProcessor implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    // 头条热榜接口地址
    public static final String URL = "https://www.toutiao.com/hot-event/hot-board/?origin=toutiao_pc";

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private SpToutiaoRbMapper spToutiaoRbMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Json json = page.getJson();
        Map map = JSONUtil.stringToObject(json.toString(), Map.class);
        Object data = map.get("data");
        List<ToutiaoHotBO> boList = JSONUtil.stringToObject(JSONUtil.objectToString(data), List.class, ToutiaoHotBO.class);
        List<SpToutiaoRb> spToutiaoRbList = Lists.newArrayList();

        if (CollectionUtils.isEmpty(boList)) {
            log.info("==========热榜数据为空，结束本次爬取任务");
            return;
        }

        boList.forEach(bo -> {
            SpToutiaoRb spToutiaoRb = new SpToutiaoRb();
            spToutiaoRb.setTitle(bo.getTitle());
            spToutiaoRb.setLink(bo.getUrl());
            spToutiaoRb.setHotValue(Long.valueOf(bo.getHotValue()));
            spToutiaoRb.setQueryWord(bo.getQueryWord());
            spToutiaoRb.setCreateTime(LocalDateTime.now());
            spToutiaoRb.setModifyTime(LocalDateTime.now());
            if (ToutiaoRbEnum.NEW.name().equalsIgnoreCase(bo.getLabel())) {
                spToutiaoRb.setState(ToutiaoRbEnum.NEW.getCode());
            } else if (ToutiaoRbEnum.HOT.name().equalsIgnoreCase(bo.getLabel())) {
                spToutiaoRb.setState(ToutiaoRbEnum.HOT.getCode());
            } else {
                spToutiaoRb.setState(ToutiaoRbEnum.NORMAL.getCode());
            }
            spToutiaoRbList.add(spToutiaoRb);
        });

        // 一条一条的插入
        spToutiaoRbList.forEach(rb -> {
            try {
                spToutiaoRbMapper.insert(rb);
            } catch (DuplicateKeyException e) {
                log.error("==========重复热榜新闻数据");
            }

            try {
                elasticsearchRestTemplate.save(rb);
            } catch (Exception e) {
                log.error("==========插入es失败");
            }
        });

    }

    @Override
    public Site getSite() {
        return site;
    }

}
