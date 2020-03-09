package org.dawix.tests.google;

import io.qameta.allure.*;
import org.dawix.listeners.AllureListener;
import org.dawix.tests.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
//Listener has to be explicitly here because AspectJ does not work well with XML configuration
// https://github.com/allure-framework/allure1/issues/730
@Test
@Listeners({AllureListener.class})
@Epic("E2E tests")
@Feature("Google search tests")
public class TestGoogle extends BaseTest {
    @Test
    @Story("I want to have a test that fails")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TESTCASE-2")
    @Link("http://something/JIRA-123")
    public void accessGoogle() {
        driver.get("https://www.google.com/ncr");

        By question = By.name("q");
        wait.until(presenceOfElementLocated(question));
        WebElement element = driver.findElement(question);

        element.sendKeys("webdriver");
        element.submit();
        //By result = By.xpath("//h3[text()='Selenium Projects']");
        //wait.until(presenceOfElementLocated(result));
        Assert.fail();
    }

    @Test
    //@Step("Search google and pass")
    @Story("I want a passing test")
    @TmsLink("TESTCASE-2")
    public void testGoogle() {
        driver.get("https://www.google.com/ncr");

        By question = By.name("q");
        wait.until(presenceOfElementLocated(question));
        WebElement element = driver.findElement(question);

        element.sendKeys("webdriver");
        element.submit();
        //By result = By.xpath("//h3[text()='Selenium Projects']");
        //wait.until(presenceOfElementLocated(result));
    }
}
