package stepdefintions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.ui.LoginHomePage;

import java.io.IOException;

public class Hooks extends CommonVariablesAndMethods{
    WebDriver driver;

    public Hooks()
    {
        this.driver = CommonVariablesAndMethods.getInstance();
    }
    @AfterStep
    public static void tearDown(Scenario scenario) {

            final byte[] screenshot = ((TakesScreenshot) CommonVariablesAndMethods.getInstance()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());

    }



}