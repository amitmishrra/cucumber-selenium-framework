package common;

import com.twilio.base.ResourceSet;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Utils {
    public static String browser, stack;
    static Properties prop = new Properties();
    public static WebDriver driver;
//    public static final String ACCOUNT_SID = "";
//    public static final String AUTH_TOKEN = "";

    public Utils(WebDriver _driver) {
        this.driver = _driver;
    }

    public static String getBrowser() {

        if (browser == null) {
            browser = System.getProperty("chrome", "firefox").toLowerCase();
        }
        return System.getProperty("browser");
    }

    public static String getStack() {
        if (stack == null) {
            stack = System.getProperty("stack", "qa").toLowerCase();
        }
        return stack;
    }

    public static String getPropertyData(String key) throws IOException {
        MatcherAssert.assertThat(getStack(), anyOf(is("qa"), is("dev"), is("staging")));
        String path = new File("src/test/resources/application-" + getStack() + ".properties").getAbsolutePath();
        prop.load(new FileInputStream(path));
        Assert.assertNotNull(prop.getProperty(key), "[[TEST SCRIPT ISSUE]] There is no value for the key: " + key + " in the properties file. Kindly re check the key.");
        return prop.getProperty(key);
    }

    public static String getCredentials(String key) throws IOException {
        MatcherAssert.assertThat(getStack(), anyOf(is("qa"), is("dev"), is("staging")));
        String path = new File("src/test/resources/creds-" + getStack() + ".properties").getAbsolutePath();
        prop.load(new FileInputStream(path));
        Assert.assertNotNull(prop.getProperty(key), "[[TEST SCRIPT ISSUE]] There is no value for the key: " + key + " in the properties file. Kindly re check the key.");
        return prop.getProperty(key);
    }


    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "PostmanRuntime/7.26.8")
                .setUrlEncodingEnabled(false).setRelaxedHTTPSValidation()
                .build();
    }

    private static void setImplicitWait(WebDriver driver, long timeOutInSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOutInSeconds));
    }

    public static boolean isElementPresent(WebElement element) {

        try {
            waitForWebElementToBeVisible(element);
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {

            Assert.fail("Element not found.");
            return false;
        }
    }

    public static void scrollToElement(WebElement expButtonToClick) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", expButtonToClick);
    }

    public static void waitForWebElementToBeVisible(WebElement locator) {
        setImplicitWait(driver, 30);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOf(locator));
        } catch (Exception e) {
            Assert.fail("Element not found with locator: ");
        }
    }

    public static void waitForWebElementToBeClickable(WebElement locator) {
        setImplicitWait(driver, 200);
        try {
            waitForWebElementToBeVisible(locator);
            fluentWait(2000);
            new WebDriverWait(driver, Duration.ofSeconds(120))
                    .until(ExpectedConditions.elementToBeClickable(locator));
            log.info("Element is clickable");
        } catch (Exception e) {
            Assert.fail("Element not found with locator: ");
            System.out.println("Element not found with locator: " + e.getMessage());
        }
    }

    public static String getStringResponse(String response, String path) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getString(path);
    }

    public static List getListResponse(String response, String path) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getList(path);
    }

    public static void fluentWait(int milliseconds) {
        Date date = new Date();
        long timeMilli = date.getTime();
        long currentDate = 0;
        do {
            Date date2 = new Date();
            currentDate = date2.getTime();
        } while (currentDate - timeMilli < milliseconds);
    }

    public static void switchToAlertAccept() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            ;
            log.error(String.format("Exception {%s} occurred while accepting the alert", e.getMessage()));
        }
    }

    public static void screenshot(String fileName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(scrFile, new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void jsClick(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log.error(String.format("Exception {%s} occurred while clicking on the element", e.getMessage()));
        }
    }

    public void closeBrowser() {
        driver.close();
    }

    public void waitToBeVisible(By element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public void waitToBeClickable(By element, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void scrollDown(int scroll) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + scroll + ")", "");
    }

    public void navigateBack() {
        String message = String.format("Successfully navigated back");
        try {
            driver.navigate().back();
            log.info(message);
        } catch (Exception e) {
            log.error(String.format("Exception {%s} occurred while navigating back", e.getMessage()));
        }
    }

    public static void mouseOver(WebElement locator) {
        try {
            waitForWebElementToBeVisible(locator);
            Actions action = new Actions(driver);
            action.moveToElement(locator).build().perform();
        } catch (Exception e) {
            log.error(String.format("Exception {%s} occurred while mouse over on element located by {%s}",
                    e.getMessage(), locator));
        }
    }

    public static String getText(WebElement locator) {
        String text = "";
        try {
            waitForWebElementToBeVisible(locator);
            text = locator.getText();
            log.info(String.format("Successfully got text {%s} of element", text));
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while getting text of element located by {%s}",
                    e.getMessage(), locator));
        }
        return text;
    }

    public static void sendKeys(WebElement locator, String value) {
        try {
            waitForWebElementToBeVisible(locator);
            locator.clear();
            locator.sendKeys("");
            locator.sendKeys(value);
            log.info("Successfully entered text");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while setting value to element with locator {%s}",
                    e.getMessage(), locator));
        }
    }

    public void switchToFrame(String frameName) {
        try {
            driver.switchTo().frame(frameName);
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while switching to frameName: {%s}",
                    e.getMessage(), frameName));
        }
    }

    public void switchToDefault() {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            log.error(String.format("Exception {%s} occurred while switching to default", e.getMessage()));
        }
    }

    public static void click(WebElement element) {
        try {
            waitForWebElementToBeClickable(element);
            element.click();
            log.info("Successfully entered text");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while setting value to element with locator {%s}",
                    e.getMessage(), element));
        }
    }

    public static void switchToOpenWindow(String windowTitle) {
        Set<String> handle = driver.getWindowHandles();
        String test1 = "";
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
            if (driver.getTitle().equalsIgnoreCase(windowTitle)) {
                break;
            }
        }
    }

    public static String randomEmailGenerator() {
        String initial = "kaliberlabs" + randomNumber() + "@mailsac.com";
        return initial;
    }

    public static String randomNumber() {
        Random random = new Random();
        String number = String.format("%04d", random.nextInt(10000));
        return number;
    }

    public static String randomPhoneNumber() {
        Random rand = new Random();
        int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);
        DecimalFormat df3 = new DecimalFormat("000");
        DecimalFormat df4 = new DecimalFormat("0000");
        String phoneNumber = "(" + df3.format(num1) + ")" + " " + df3.format(num2) + "-" + df4.format(num3);
        return phoneNumber;
    }

    public static void assertEqual(String actual, String expected) {
        try {
            Assert.assertEquals(actual, expected);
            log.info(String.format("Actual {%s} and Expected {%s} are equal", actual, expected));
        } catch (AssertionError e) {
            log.error(String.format("Actual {%s} and Expected {%s} are not equal", actual, expected));
        }
    }

    public static String getCurrentDate() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String date = dateObj.format(formatter);
        return date;
    }

    public static String getCurrentTime() {
        LocalTime timeObj = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = timeObj.format(formatter);
        return time;
    }

    public static RequestSpecification mailSacRequestSpecification() {
        String mailsackey = System.getProperty("mailsackey",
                "k_29oSwkOhqth4MIcx5tfzNWkNHON8174ab");
        return new RequestSpecBuilder().setBaseUri("https://mailsac.com/api")
                .addHeader("Accept", "application/json")
                .addQueryParam("_mailsacKey", mailsackey).build();
    }

    public static ArrayList<String> getEmailVerificationCode(String email) {
        fluentWait(20000);
        ArrayList<String>  list =  new ArrayList<>();
        try {
            String body = given(mailSacRequestSpecification()).pathParam("email", email)
                    .when().get("/addresses/{email}/messages").then().extract().response().getBody().asString();
            System.out.println(body);
              list = (ArrayList<String>) getListResponse(body, "[0].links");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while getting email verification code", e.getMessage()));
        }
        return list;
    }

    public static String getSubjectFromMail(String mail) {
        String subject = "";
        try {
            String body = given(mailSacRequestSpecification()).pathParam("email", mail)
                    .when().get("/addresses/{email}/messages").then().extract().response().getBody().asString();
            subject = getStringResponse(body, "[0].subject");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while getting email verification code", e.getMessage()));
        }
        return subject;
    }

    public static void assertNotNull(Object obj) {
        try {
            Assert.assertNotNull(obj);
            log.info(String.format("Object {%s} is not null", obj));
        } catch (AssertionError e) {
            log.error(String.format("Object {%s} is null", obj));
        }
    }

    public static void refreshPage() {
        try {
            driver.navigate().refresh();
            log.info("Page refreshed");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while refreshing page", e.getMessage()));
        }
    }

    public static void clickOnCoordinates(int x, int y) {
        try {
//            waitForWebElementToBeVisible(element);
//            int x = element.getLocation().getX();
//            int y = element.getLocation().getY();
            System.out.println("x: " + x + " y: " + y);
            Actions action = new Actions(driver);
            action.moveByOffset(x, y).click().build().perform();
            log.info("Successfully clicked on element");
        } catch (Exception e) {
//            log.error(String.format("Exception {%s} happened while clicking on element with locator {%s}",
//                    e.getMessage(), element));
        }
    }

    public static String randomNPI() {
        Random rand = new Random();
        int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);
        DecimalFormat df3 = new DecimalFormat("000");
        DecimalFormat df4 = new DecimalFormat("0000");
        return df3.format(num1) + df3.format(num2) + df4.format(num3);
    }

    public static void openUrlInNewTab(String url) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.open()");
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            driver.get(url);
            log.info("Successfully opened url in new tab");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while opening url in new tab", e.getMessage()));
        }
    }

    public static void closeCurrentTab() {
        try {
            driver.close();
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(0));
            log.info("Successfully closed current tab");
        } catch (Exception e) {
            log.error(String.format("Exception {%s} happened while closing current tab", e.getMessage()));
        }
    }

    public static boolean isElementVisible(WebElement element) {
        //This method is used to check if element is visible or not
        boolean flag = false;
        try {
            element.isDisplayed();
            flag = true;
        } catch (Exception e) {
            log.error(String.format("Element {%s} is not visible", element));
            flag = false;
        }
        return flag;
    }


    public static String getMessage() throws IOException {
        Twilio.init(getCredentials("ACCOUNT_SID"), getCredentials("AUTH_TOKEN"));
        String phoneNumber = getCredentials("TWILIO_NUMBER");
        ResourceSet<Message> messages = Message.reader()
                .setTo(new PhoneNumber(phoneNumber))
                .read();

        String url = "";
        // Iterate over the retrieved messages
        for (Message message : messages) {
            url = extractURL(message.getBody());
        }
        return url;
    }

    public static String addImages(String [] img){
        //PUT ALL IMAGES IN PROJECT FOLDER
        String path = "";
        if (img.length == 1){
            File file = new File(img[0]);
             path = file.getAbsolutePath();
        }else{
            for(int i=0; i<img.length; i++){
                File file = new File(img[i]);
                path = path + file.getAbsolutePath() + "\n";
            }
        }
        return path;
    }

    public static String extractURL(String text){
        String url = "";
        String regex = "(https?://\\S+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
             url = matcher.group();
            System.out.println("Extracted URL: " + url);
        } else {
            System.out.println("No URL found in the text.");
        }
        return url;
    }

    public static void isTextPresent(String text){
        WebElement textElement = driver.findElement(By.xpath("//*[contains(text(),'" + text + "')]"));
        isElementPresent(textElement);
    }


}




