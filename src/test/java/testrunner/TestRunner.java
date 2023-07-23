package testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/Features/LoginTest.feature",
        glue = "stepdefintions",
        plugin = {"html:target/cucumber-report.html"})

public class TestRunner  {}