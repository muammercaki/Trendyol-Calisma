package com.trendyol.step;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import helper.ElementHelper;
import helper.StoreHelper;
import model.ElementInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class StepImlementation extends Driver {
    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;
    private int invalidImageCount;
    protected static Logger logger = LogManager.getLogger(StepImlementation.class);


    @Step("Go to url <url>")
    public static void goToUrl(String url) {
        driver.get(url);
        logger.info("Gidilen url: " + url);
    }


    @Step("Hover to element <element>")
    public static void hoverElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    @Step("Click to element <key>")
    public void clickElement(String key) {
        WebElement element = findElement(key);
        scrollToElement(element);
        hoverElement(element);
        clickElement(element);
    }

    @Step("Ad basket js click")
    public void adBasketJsClick() {

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.querySelector(\"div[class='add-to-bs-tx']\").click();");


    }


    @Step("Write value <text> to element <key>")
    public void writeText(String text, String key) {
        if (!key.equals("")) {
            findElement(key).clear();
            findElement(key).sendKeys(text);
        }
    }

    @Step("Check if element <key> exists")
    public WebElement getElementWithKeyIfExists(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                return driver.findElement(by);
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        logger.info("Element exists..");
        return null;
    }

    @Step("Wait <value> seconds")
    public void waitBySeconds(int seconds) {
        try {
            logger.info(seconds + " wait second.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    @Step("Klavye ile <downLoop> kere asagi kaydir")
    public void keysDOWNHaberDetay(int downLoop) {

        for (int i = 0; i < downLoop; i++) {
            try {

                driver.findElement(By.tagName("body")).sendKeys(Keys.chord(Keys.DOWN));
            } catch (Exception e) {

                logger.info("sıkıntı yok devam..");
            }
        }
        logger.info(downLoop + " Kere asagi kaydırıldı.");
    }

    @Step("Check image <element>")
    public void CheckImage(String element) throws Exception {

        try {
            invalidImageCount = 0;
            List<WebElement> imagesList = findElements(element);
            logger.info("Total no. of images are " + imagesList.size());
            for (WebElement imgElement : imagesList) {
                if (imgElement != null) {
                    verifyimageActive(imgElement);
                }
            }
            logger.info("Total no. of invalid images are " + invalidImageCount);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Step("Random element <elements> click")
    public void randomClick(String elements) {
        List<WebElement> lastNewsTitle = findElements(elements);
        WebElement randomNews = (WebElement) getRandomContent(lastNewsTitle);
        scrollToElement(randomNews);
        clickElement(randomNews);
    }


    @Step("Scroll to find element <key>")
    public void findScroll(String key) {
        WebElement element = findElement(key);
        scrollToElement(element);
    }

    @Step("Hard click <key>")
    public void hardClick(String key) {
        WebElement element = findElement(key);
        waitByMilliSeconds(500);
        clickElement(element);
    }

    @Step("Wait <value> milliseconds")
    public void waitByMilliSeconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Page to refresh")
    public void pageRefresh(){
        driver.navigate().refresh();
    }

    @Step("Focus new tab")
    public void
    focusNewTab() {
        ArrayList<String> tabList = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabList.get(tabList.size() - 1));

    }

    public void scrollToElement(WebElement element) {

        if (element != null) {
            scrollTo(element.getLocation().getX(), element.getLocation().getY() - 350);
            waitBySeconds(2);

        }
    }

    protected void scrollTo(int x, int y) {
        String jsStmt = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(jsStmt, true);

    }

    protected Object executeJS(String jsStmt, boolean wait) {
        return wait ? getJSExecutor().executeScript(jsStmt, "")
                : getJSExecutor().executeAsyncScript(jsStmt, "");
    }

    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    public Object getRandomContent(List<?> contentList) {
        Random rand = new Random();
        int n = rand.nextInt(contentList.size());
        return contentList.get(n);
    }

    public void verifyimageActive(WebElement imgElement) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(imgElement.getAttribute("src"));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200)
                invalidImageCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickElement(WebElement element) {
        javaScriptClicker(driver, element);
    }

    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);" + "arguments[0].style.border='6px dotted blue'", element);
    }

    private WebElement findElement(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
            WebDriverWait webDriverWait = new WebDriverWait(driver, 6, 300);
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.presenceOfElementLocated(infoParam));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                    webElement);
            return webElement;

        } catch (TimeoutException e) {
            Assert.fail("Verilen Sürede Aranan Eleman : '" + key + "' Oluşmamıştır.");
            return null;
        }


    }

    private List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return driver.findElements(infoParam);
    }

}
