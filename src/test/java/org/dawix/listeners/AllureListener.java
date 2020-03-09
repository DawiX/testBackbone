package org.dawix.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.dawix.tests.common.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener extends BaseTest implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START " + getTestMethodName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("SUCCESS " + getTestMethodName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAIL " + getTestMethodName(result));

        Object testClass = result.getInstance();
        RemoteWebDriver webDriver = ((BaseTest) testClass).getDriver();

        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) webDriver).
                getScreenshotAs(OutputType.BASE64);
        Allure.addAttachment("Screenshot", base64Screenshot);
        saveScreenshotPNG(webDriver);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIP " + getTestMethodName(result));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("IN SUCCESS RATIO " + getTestMethodName(result));
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("START METHOD " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("FINISH METHOD " + context.getName());
    }
}
