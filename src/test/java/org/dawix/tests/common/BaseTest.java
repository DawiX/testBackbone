package org.dawix.tests.common;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URL;


public class BaseTest {
    public static RemoteWebDriver driver;
    public static WebDriverWait wait;

    public RemoteWebDriver getDriver() {
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser_name"})
    public void setup(String browser_name, ITestContext ctx) throws Exception {
        String suiteName = ctx.getCurrentXmlTest().getSuite().getName();
        String HUB_HOST = "localhost";
        if (System.getenv("SELENIUM_HUB_HOST") != null) {
            HUB_HOST = System.getenv("SELENIUM_HUB_HOST");
        }
        System.out.println(HUB_HOST);
        DesiredCapabilities capabilities = getBrowserCapabilities(browser_name, false);
        // Zalenium creating video under sessionID name
        //capabilities.setCapability("name", suiteName);
        capabilities.setCapability("testFileNameTemplate", "myID_{seleniumSessionId}_{testStatus}");
        driver = new RemoteWebDriver(new URL("http://" + HUB_HOST + ":4444/wd/hub"), capabilities);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        wait = new WebDriverWait(driver, 30);
    }

    public static DesiredCapabilities getBrowserCapabilities(String driverParameter, Boolean headless) {
        DesiredCapabilities capabilities = null;
        if (driverParameter == null || driverParameter.equalsIgnoreCase("CHROME")) {
            capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-infobars");
            options.setAcceptInsecureCerts(true);
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setHeadless(headless);
            capabilities.merge(options);
        }
        else if (driverParameter.equalsIgnoreCase("FIREFOX")) {
            capabilities = DesiredCapabilities.firefox();
            FirefoxOptions options = new FirefoxOptions();
            options.setAcceptInsecureCerts(true);
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            options.addPreference("browser.tabs.remote.autostart", false);
            options.setHeadless(headless);
        }
        return capabilities;
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
