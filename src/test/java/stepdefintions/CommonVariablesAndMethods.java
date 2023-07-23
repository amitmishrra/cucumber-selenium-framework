package stepdefintions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

import static common.Utils.*;
import static common.Utils.sendKeys;

public class CommonVariablesAndMethods {
    public static WebDriver driver;

    public static WebDriver getInstance() {

        if (driver != null) {
            return driver;
        }
        if ("firefox".equalsIgnoreCase(getBrowser())) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-private");
            options.addArguments("--disable-extensions");
            driver = new FirefoxDriver(options);

        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
//            options.setHeadless(!SystemUtils.IS_OS_WINDOWS);
//            options.addArguments("incognito");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-infobars");
            options.addArguments("--enable-automation");
            options.addArguments("--window-size=1200,600");
            options.addArguments("--disable-gpu");
            options.addArguments("--dns-prefetch-disable");
            options.addArguments("--enable-features=NetworkServiceInProcess");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().window().maximize();
        return driver;
    }

    public static void buttonClick(String btnText) {
        By btn = By.xpath("//*[text()='" + btnText + "']");
        WebElement btnClickAction = driver.findElement(btn);
        click(btnClickAction);
    }

}
