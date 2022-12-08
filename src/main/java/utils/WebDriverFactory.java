package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WebDriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver createChromeBrowser() {
        logger.info("Configurando WebDriver...");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions().addArguments("-force-device-scale-factor=1.0")
//										.addArguments("test-type")
                .addArguments("--disable-popup-blocking").addArguments("--disable-web-security")
                .addArguments("--disable-notifications")
//										.addArguments("--disable-session-crashed-bubble")
                .addArguments("--profile-directory=Default")
//										.addArguments("--user-data-dir=C:/Temp/ChromeProfile")
                .addArguments("start-maximized");
        logger.debug(options.toString());
        logger.info("Instanciando novo driver");
        WebDriver browser = new ChromeDriver(options);
        browser.manage().timeouts().implicitlyWait(Duration.ofMillis(200));
        return browser;

    }

}
