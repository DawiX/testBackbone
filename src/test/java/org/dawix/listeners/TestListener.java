package org.dawix.listeners;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dawix.tests.common.BaseTest;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener extends BaseTest implements ITestListener, ISuiteListener {

    private static Logger log = LogManager.getLogger("TestListener");

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("I am in onTestStart method " +  getTestMethodName(result) + " start");
        System.out.println(driver.getSessionId());
        //Cookie name = new Cookie("zaleniumMessage", getTestMethodName(result));
        //driver.manage().addCookie(name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("I am in onTestSuccess method " +  getTestMethodName(result) + " succeed");
        Cookie cookie = new Cookie("zaleniumTestPassed", "true");
        driver.manage().addCookie(cookie);

        Cookie name = new Cookie("zaleniumMessage", getTestMethodName(result));
        driver.manage().addCookie(name);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("I am in onTestFailure method " +  getTestMethodName(result) + " failed");

        Object testClass = result.getInstance();
        RemoteWebDriver webDriver = ((BaseTest) testClass).getDriver();

        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) webDriver).
                getScreenshotAs(OutputType.BASE64);
        Reporter.log("<p>" + result.getThrowable().toString() + "</p>");
        Reporter.log("<img src=\"" + base64Screenshot + "\"/>");
        Cookie cookie = new Cookie("zaleniumTestPassed", "false");
        driver.manage().addCookie(cookie);

        String videoUrl = "http://localhost:4444/dashboard/" + "myID_" + driver.getSessionId() + "_FAILED" + ".mp4";
        Reporter.log("<a href=\"" + videoUrl + "\">Video here</a>");

        Cookie name = new Cookie("zaleniumMessage", getTestMethodName(result));
        driver.manage().addCookie(name);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("I am in onTestSkipped method " + getTestMethodName(result) + " skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(result));
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("I am in onStart method " + context.getName());
        context.setAttribute("WebDriver", this.driver);
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("I am in onFinish method " + context.getName());
    }

    // for ISuiteListener
    @Override
    public void onStart(ISuite suite) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:MM");
        LocalDateTime now = LocalDateTime.now();
        String suiteName = suite.getName();

        int repeatNumber = 29;

        if (suiteName.length() > 28) {
            repeatNumber = suiteName.length() + 3;
        }

        log.info(StringUtils.repeat("-", repeatNumber));
        log.info("| Execution " + dtf.format(now));
        log.info("| " + suiteName);
        log.info(StringUtils.repeat("-", repeatNumber));
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info(" ");

    }
}
