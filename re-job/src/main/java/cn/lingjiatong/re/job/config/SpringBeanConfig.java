package cn.lingjiatong.re.job.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;

/**
 * bean配置
 *
 * @author Ling, Jiatong
 * Date: 2022/10/12 22:59
 */
@Configuration
public class SpringBeanConfig {

    
    // 浏览器
    @Bean(name = "chromeWebDriver")
    public WebDriver webDriver() {
        // TODO 后期改为使用池化技术
        // 设置系统环境变量
        String osName = System.getProperty("os.name");
        WebDriver webDriver;
        // 开发环境 Mac
        if (osName.contains("Mac")) {
            String webDirverHome = "/Users/lingjiatong/software/chromedriver";
            System.setProperty("webdriver.chrome.driver", webDirverHome);
            ChromeOptions chromeOptions = new ChromeOptions();
            // 启用所有类型的日志并收集所有日志
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
            chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            // 不加载图片
//            chromeOptions.addArguments("blink-settings=imagesEnabled=false");
            // 生成浏览器
            webDriver = new ChromeDriver(chromeOptions);
        } else {
            // 生产环境 linux
            String webDirverHome = "/usr/bin/chromedriver";
            System.setProperty("webdriver.chrome.driver", webDirverHome);
            /* linux */
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1920,1050");
            // 禁用沙盒
            options.addArguments("no-sandbox");
            // 禁止加载图片
//            options.addArguments("blink-settings=imagesEnabled=false");
            options.addArguments("--whitelisted-ips=\"\"");
            options.addArguments("--disable-dev-shm-usage");
            // 无界面模式 在Linux中一定是不能唤起浏览器的（很重要）
            options.setHeadless(Boolean.TRUE);
            webDriver = new ChromeDriver(options);
        }
        return webDriver;
    }
}
