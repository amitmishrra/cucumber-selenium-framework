package pages.ui;

import common.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import stepdefintions.CommonVariablesAndMethods;

import java.io.IOException;

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

    public void navigate() throws IOException {
        driver.get(getPropertyData("ui.baseurl"));
    }

    public void loginPage(String username, String password) {
        sendKeys(emailText, username);
        sendKeys(passwordText, password + Keys.ENTER);

    }
}


