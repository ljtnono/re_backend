package cn.lingjiatong.re.job.crawler;

import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.job.bo.ToutiaoHotBO;
import cn.lingjiatong.re.job.entity.SpToutiaoRb;
import cn.lingjiatong.re.job.enumeration.ToutiaoRbEnum;
import cn.lingjiatong.re.job.mapper.SpToutiaoRbMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
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
            spToutiaoRbMapper.insert(rb);
            elasticsearchRestTemplate.save(rb);
        });

//        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
//            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//
//        // 部分三：从页面发现后续的url地址来抓取
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        Spider.create(new ToutiaoHotProcessor())
//                //从"https://github.com/code4craft"开始抓
//                .addUrl("https://github.com/code4craft")
//                //开启5个线程抓取
//                .thread(5)
//                //启动爬虫
//                .run();
//    }
}
