package pages.ui;

import common.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import stepdefintions.CommonVariablesAndMethods;

import java.time.Duration;
import java.util.List;

import static common.Utils.*;
import static stepdefintions.CommonVariablesAndMethods.*;

public class LoginHomePage {
    WebDriver driver = CommonVariablesAndMethods.getInstance();
    String userName = "";
    Utils utils;
    String mail = "";

    String verifyEmailUrl = "";

    @FindBy(how = How.ID_OR_NAME, using = "username")
    public static WebElement emailText;

    @FindBy(how = How.ID, using = "password")
    public static WebElement passwordText;

    @FindBy(how = How.XPATH, using = "//*[@class='profile-text']")
    public static WebElement profileText;
    @FindBy(how = How.XPATH, using = "//*[@class='MuiIconButton-label']")
    public static WebElement termsAndConditionsCheckbox;


    public LoginHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.utils = new Utils(driver);
    }

    public void loginPage(String username, String password) {
        sendKeys(emailText, username);
        sendKeys(passwordText, password + Keys.ENTER);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By termsOfUseLocator = By.xpath("//*[text()='Kaliber Labs Terms of Use']");
        WebElement termsOfUseElement = null;

        try {
            termsOfUseElement = wait.until(ExpectedConditions.visibilityOfElementLocated(termsOfUseLocator));
        } catch (TimeoutException e) {
            System.out.println("No terms and conditions checkbox");
        }

        if (termsOfUseElement != null) {
            fluentWait(5000);
           driver.findElement(By.xpath("//*[@class='jss4']")).click();
            driver.findElement(By.xpath("//*[text()='Continue']")).click();
        }
    }
}


