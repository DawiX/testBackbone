package org.dawix.tests.google;

import org.dawix.listeners.AllureListener;
import org.dawix.tests.common.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
//Listener has to be explicitely here beause AspectJ does not work well with XML configuration
// https://github.com/allure-framework/allure1/issues/730
@Test
@Listeners({AllureListener.class})
public class TestGoogle extends BaseTest {
    @Test
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
