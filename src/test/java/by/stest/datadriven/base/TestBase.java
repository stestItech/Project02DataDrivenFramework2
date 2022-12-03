package by.stest.datadriven.base;

import by.stest.utilities.ExtentManager;
import by.stest.utilities.TestUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class TestBase {

    public RemoteWebDriver driver = null;
    public static ThreadLocal<RemoteWebDriver> dr = new ThreadLocal<>();
    public Properties or = new Properties();
    public Properties config = new Properties();
    public FileInputStream fis;
    public static Logger log2 = LogManager.getLogger(TestBase.class.getName());
    public static ExtentReports extent = ExtentManager.createInstance();
    public static ExtentTest test;
    public static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    public String browser;
    public static ThreadLocal<String> browserThread = new ThreadLocal<>();

    public void setUp() {
        if (driver == null) {

            // Unable to set up logs through System.setProperty
            /*System.setProperty("log4j2.configurationFile",
                    "./resources/properties/log4j2.properties");
            System.setProperty("log4j2.configurationFile",
                    "src/test/resources/properties/log4j2.properties");*/
            File f = new File("src/test/resources/properties/log4j2.properties");
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.setConfigLocation(f.toURI());

            try {
                fis = new FileInputStream("src//test//resources//properties//Config.properties");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                config.load(fis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                fis = new FileInputStream("src//test//resources//properties//OR.properties");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                or.load(fis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void openBrowser(String browser) throws MalformedURLException {
        setThreadBrowser(browser);
        Capabilities cap = null;

        if (browser.equals("firefox")) {
            cap = new FirefoxOptions();
        } else if (browser.equals("chrome")) {
            cap = new ChromeOptions();
        } else if (browser.equals("edge")) {
            cap = new EdgeOptions();
        }

        //driver = new RemoteWebDriver(new URL("http://192.168.0.75:4444/"), cap);
        driver = new RemoteWebDriver(new URL("http://13.48.85.142:4444"), cap);
        // localhost
        setWebDriver(driver);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds
                (Integer.parseInt(config.getProperty("implicit.wait"))));
        getDriver().manage().window().maximize();

        this.browser = browser;
        getExtentTestThread().log(Status.INFO, browser + " was opened successfully");
        ThreadContext.put("thread", getThreadValue(dr.get()));
        ThreadContext.put("browser", browser);
    }

    public void reportPass(String message) {
        getExtentTestThread().log(Status.PASS, message);
    }

    public void reportFailure(String message) {
        getExtentTestThread().log(Status.FAIL, message);
        try {
            TestUtils.captureScreenshot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.fail(message);
    }

    public static WebDriver getDriver() {
        return dr.get();
    }

    public void setWebDriver(RemoteWebDriver driver) {
        dr.set(driver);
    }

    public static String getThreadBrowser() {
        return browserThread.get();
    }

    public void setThreadBrowser(String browser) {
        browserThread.set(browser);
    }

    public void setExtentTestThread(ExtentTest et) {
        testThread.set(et);
    }

    public static ExtentTest getExtentTestThread() {
        return testThread.get();
    }

    public void addLog(String message) {
        log2.debug("Thread: " + getThreadValue(dr.get()) +
                " Browser: " + getThreadBrowser() + " " + message);
    }

    public String getThreadValue(Object value) {
        String threadText = value.toString();
        String[] threadArray = threadText.split(" ");
        String threadText2 = threadArray[threadArray.length-1]
                .replace("(","")
                .replace(")","");
        String[] threadArray2 = threadText2.split("-");
        return threadArray2[threadArray2.length-1]
                .substring(threadArray2[threadArray2.length-1].length() - 12);
    }

    public void navigateTo(String url) {
        getDriver().get(config.getProperty(url));
        getExtentTestThread().log(Status.INFO, "Navigating to " + config.getProperty(url));
    }

    public void click(String locator) {
        try {
            if (locator.endsWith("_CSS")) {
                getDriver().findElement(By.cssSelector(or.getProperty(locator))).click();
            } else if (locator.endsWith("_XPATH")) {
                getDriver().findElement(By.xpath(or.getProperty(locator))).click();
            } else if (locator.endsWith("_ID")) {
                getDriver().findElement(By.id(or.getProperty(locator))).click();
            }
            //getThreadLog().info("Clicking on an Element: {}", locator);
            addLog("Clicking on an Element: " + locator);
        } catch (Throwable t) {
            reportFailure("Failed while clicking on element " + locator);
        }
        getExtentTestThread().log(Status.INFO,
                "Element " + or.getProperty(locator) + " was clicked");
    }

    public void type(String locator, String value) {
        try {
            if (locator.endsWith("_CSS")) {
                getDriver().findElement(By.cssSelector(or.getProperty(locator))).sendKeys(value);
            } else if (locator.endsWith("_XPATH")) {
                getDriver().findElement(By.xpath(or.getProperty(locator))).sendKeys(value);
            } else if (locator.endsWith("_ID")) {
                getDriver().findElement(By.id(or.getProperty(locator))).sendKeys(value);
            }
            addLog("Typing in an element " + locator + " with value " + value);
        } catch (Throwable t) {
            reportFailure("Failed while typing in element " + locator);
        }
    }

    static WebElement dropdown;

    public void select(String locator, String value) {
        try {
            if (locator.endsWith("_CSS")) {
                dropdown = getDriver().findElement(By.cssSelector(or.getProperty(locator)));
            } else if (locator.endsWith("_XPATH")) {
                dropdown = getDriver().findElement(By.xpath(or.getProperty(locator)));
            } else if (locator.endsWith("_ID")) {
                dropdown = getDriver().findElement(By.id(or.getProperty(locator)));
            }
        } catch (Throwable t) {
            reportFailure("Failed while selecting an element " + locator);
        }

        Select select = new Select(dropdown);
        select.selectByVisibleText(value);

        getExtentTestThread()
                .log(Status.INFO, "Value in " + or.getProperty(locator) + " drop down was selected");
    }

    @AfterSuite
    public void tearDown() {
        getDriver().quit();
    }
}
