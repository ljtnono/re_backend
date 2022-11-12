package cn.lingjiatong.re.job.crawler;

import cn.lingjiatong.re.common.util.EncryptUtil;
import cn.lingjiatong.re.common.util.JSONUtil;
import cn.lingjiatong.re.common.util.SnowflakeIdWorkerUtil;
import cn.lingjiatong.re.common.util.UrlUtil;
import cn.lingjiatong.re.job.bo.BaiduImageSpiderSearchConditionBO;
import cn.lingjiatong.re.job.entity.SpBaiduImg;
import cn.lingjiatong.re.job.mapper.SpBaiduImgMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 百度图片爬虫处理器
 *
 * @author Ling, Jiatong
 * Date: 2022/10/13 17:49
 */
@Data
@Slf4j
@Component
@NoArgsConstructor
public class BaiduImageSpiderProcessor  {
    // 百度图片爬虫爬取地址
    public static final String URL = "https://image.baidu.com/search/index?&ct=201326592&tn=baiduimage&ipn=r&pn=&spn=&istype=2&ie=utf-8&oe=utf-8&cl=2&lm=-1&st=-1&fr=&fmq=&se=&sme=&face=0&cs=&os=&objurl=&di=&gsm=&";
    @Autowired
    @Qualifier("chromeWebDriver")
    private WebDriver webDriver;
    @Resource
    private SpBaiduImgMapper spBaiduImgMapper;
    private String spiderParam;
    @Autowired
    private SnowflakeIdWorkerUtil snowflakeIdWorkerUtil;
    public BaiduImageSpiderProcessor(String spiderParam) {
        this.spiderParam = spiderParam;
    }

    // 从src中获取format的正则表达式
    public static final Pattern IMG_FORMAT_REX_PATTERN = Pattern.compile("http(s)?://(.*)&f=(.*)\\?");

    public void process() throws InterruptedException {
        if (StringUtils.isEmpty(spiderParam)) {
            spiderParam = BaiduImageSpiderSearchConditionBO.ONLY_SEARCH_CONDITION_PREFIX + "美图";
        }
        String u = URL + spiderParam;
        log.info("==========完整请求URL：{}", u);
        // 发起请求
        webDriver.get(u);
        Thread.sleep(2000);
        int oldPageCount = webDriver.findElements(By.xpath("//*[@id=\"imgid\"]/div")).size();
        if (0 == oldPageCount) {
            // 没找到信息
            return;
        }
        int currentPageCount = 1;
        while (true) {
            List<WebElement> imgElements = webDriver.findElements(By.xpath("//*[@id=\"imgid\"]/div[" + currentPageCount + "]/ul/li[@class=\"imgitem\"]/div[1]/div[2]/a/img"));
            List<WebElement> aElements = webDriver.findElements(By.xpath("//*[@id=\"imgid\"]/div[" + currentPageCount + "]/ul/li[@class=\"imgitem\"]/a[contains(@class,\"imgitem-title\")]"));
            for (int i = 0; i < imgElements.size(); i++) {
                WebElement imgElement = imgElements.get(i);
                String title = aElements.get(i).getAttribute("title");
                if (StringUtils.isEmpty("title")) {
                    title = "暂无";
                }
                String src = imgElement.getAttribute("src");
                Map<String, String> paramter = UrlUtil.getUrlQueryParamter(src);
                log.info("==========src地址的查询参数列表：{}", JSONUtil.objectToString(paramter));
                SpBaiduImg spBaiduImg = new SpBaiduImg();
                spBaiduImg.setId(snowflakeIdWorkerUtil.nextId());
                Matcher matcher = IMG_FORMAT_REX_PATTERN.matcher(src);
                if (matcher.find()) {
                    spBaiduImg.setFormat(matcher.group(3));
                }
                spBaiduImg.setSrc(src);
                String height = paramter.get("h");
                String width = paramter.get("w");
                if (!StringUtils.isEmpty(height)) {
                    spBaiduImg.setHeight(Integer.valueOf(height));
                }
                if (!StringUtils.isEmpty(width)) {
                    spBaiduImg.setWidth(Integer.valueOf(width));
                }
                spBaiduImg.setTitle(title);
                spBaiduImg.setCreateTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                spBaiduImg.setModifyTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                String uniqueMd5Key = new StringBuilder()
                        .append(title)
                        .append(src)
                        .append(StringUtils.isEmpty(width) ? "NULL" : width)
                        .append(StringUtils.isEmpty(height) ? "NULL" : height)
                        .append(StringUtils.isEmpty(spBaiduImg.getFormat()) ? "NULL" : spBaiduImg.getFormat())
                        .toString();
                spBaiduImg.setUniqueMd5(EncryptUtil.getInstance().getMd5(uniqueMd5Key));
                try {
                    spBaiduImgMapper.insert(spBaiduImg);
                } catch (DuplicateKeyException e) {
                    log.error("==========重复图片");
                }
            }
            ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + (currentPageCount * 100000) + ")");
            Thread.sleep(1000);
            currentPageCount = webDriver.findElements(By.xpath("//*[@id=\"imgid\"]/div")).size();
            if (currentPageCount > oldPageCount) {
                oldPageCount++;
            } else {
                break;
            }
        }
    }
}
