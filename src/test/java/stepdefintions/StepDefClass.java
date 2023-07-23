package stepdefintions;

import common.Utils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.ui.LoginHomePage;

public class StepDefClass {
    WebDriver driver;
    LoginHomePage loginHomePage;
    Utils utils;
    public StepDefClass()
    {
        this.driver = CommonVariablesAndMethods.getInstance();
        this.loginHomePage = new LoginHomePage(driver);
    }
    @When("User logs in as Surgeon {string} and {string}")

    @When("User enters {string} and {string} in the login page")
    public void userEntersAndInTheLoginPage(String username, String password) {
        loginHomePage.loginPage(username, password);
    }
}
