package org.dawix.helpers.common;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class Base {
    public RemoteWebDriver driver;
    public WebDriverWait wait;
    private Actions builder;

    public Base(RemoteWebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.builder = new Actions(driver);
    }

    /**
     * Standard click element method.
     * @param elementLocation By
     */
    public void click(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(visibilityOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        driver.findElement(elementLocation).click();
    }

    /**
     * Standard click element method with retry when findElement would throw StaleElementReferenceException
     * or NoSuchElementException exception.
     * @param elementLocation By
     * @param repeat int
     */
    public void clickWithRetry(By elementLocation, int repeat) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(visibilityOfElementLocated(elementLocation));
        for (int i = 0; i < repeat; i++) {
            try {
                wait.until(elementToBeClickable(elementLocation));
                driver.findElement(elementLocation).click();
                return;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                System.out.println("Retry click");
            }
        }
    }

    /**
     * Elements created by GWT do not have .click() event.
     * Click is emulated by sending hover, hold and release.
     * @param elementLocation By
     */
    public void gwtClick(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        WebElement element = driver.findElement(elementLocation);

        builder.moveToElement(element)
                .clickAndHold(element)
                .release()
                .build()
                .perform();
    }

    /**
     * Some elements are displayed only when hovered on.
     * Method waits for element, hovers on the element and then sends click.
     * @param elementLocation By
     */
    public void actionClick(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        WebElement element = driver.findElement(elementLocation);

        builder.moveToElement(element)
                .click(element)
                .build()
                .perform();
    }

    /**
     * Some elements use ng-click attribute and selenium does not handle that well.
     * This methods mocks a JS click event on such element.
     * @param elementLocation
     */
    public void javaScriptClick(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        WebElement element = driver.findElement(elementLocation);

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    /**
     * Simple sendKeys method wrapper that adds wait for element before sending keys.
     * @param elementLocation By
     * @param text String
     */
    public void writeText(By elementLocation, String text) {
        wait.until(presenceOfElementLocated(elementLocation));
        driver.findElement(elementLocation).sendKeys(text);
    }

    /**
     * getText() method wrapper with implicit wait for the element.
     * @param elementLocation By
     * @return String
     */
    public String readText(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        return driver.findElement(elementLocation).getText();
    }

    /**
     * Only reliable solution for both ChromeDriver and FirefoxDriver to change text in textarea,
     * because standard .clear() function does not work reliably.
     * Method waits for element, then gets number of chars in text area and sends the same number
     * of backspaces to clear the content followed by sendKeys() and Enter to set new text value.
     * @param elementLocation By
     * @param text String
     */
    public void textAreaRename(By elementLocation, String text) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        WebElement element = driver.findElement(elementLocation);

        int currentTextLength = readText(elementLocation).length();
        for (int i = 0; i < currentTextLength; i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(text);
        element.sendKeys(Keys.RETURN);
    }

    /**
     * Only reliable solution for both ChromeDriver and FirefoxDriver to change value in input area,
     * because standard .clear() function does not work reliably.
     * Method waits for element, gets number of chars in input area and sends the same number
     * of backspaces to clear the content followed by sendKeys() and Enter to set new value.
     * @param elementLocation By
     * @param text String
     */
    public void inputRewrite(By elementLocation, String text) {
        wait.until(presenceOfElementLocated(elementLocation));
        wait.until(elementToBeClickable(elementLocation));
        WebElement element = driver.findElement(elementLocation);

        int currentTextLength = element.getAttribute("value").length();
        for (int i = 0; i < currentTextLength; i++) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(text);
    }

    /**
     * Submit login form.
     * @param elementLocation By
     */
    public void submitLogin(By elementLocation) {
        wait.until(presenceOfElementLocated(elementLocation));
        driver.findElement(elementLocation).submit();
    }

    /**
     * Click on material-ui dropdown component and select the option by text
     * @param dropdownId DOM id of the dropdown (root div)
     * @param optionText
     */
    protected void selectFromMaterialUIDropDown(String dropdownId, String optionText) {
        By selectId = By.id(dropdownId);
        click(selectId);
        click(By.xpath("//li[@data-value='" + optionText + "' and @role='option']"));
        sleep(400);
    }

    /**
     * Click on material-ui dropdown component and select the option by index
     * @param dropdownId DOM id of the dropdown (root div)
     * @param optionIndex 1-based index of the option to click on
     */
    protected void selectFromMaterialUIDropDown(String dropdownId, int optionIndex) {
        By selectId = By.id(dropdownId);
        click(selectId);
        click(By.xpath("(//li[@role='option'])["+ optionIndex + "]"));
        sleep(400);
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Assay progress indicator uses an overlay div.
     * This function will wait until the div is not present in the DOM.
     * This can be used to avoid click intercepted errors.
     */
    protected void waitForAssayProgressIndicatorToDisappear() {
        wait.until(invisibilityOfElementLocated(By.className("CXNOverlay")));
    }
}
