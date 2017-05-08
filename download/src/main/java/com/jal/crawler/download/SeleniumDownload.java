package com.jal.crawler.download;

import com.jal.crawler.request.PageRequest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.DownAction;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by home on 2017/1/16.
 */
public class SeleniumDownload extends DynamicDownload {
    private WebDriver webDriver;

    public SeleniumDownload(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    @Override
    protected void internalDown(PageRequest pageRequest) throws IOException {
        webDriver.manage().timeouts().setScriptTimeout(1,TimeUnit.SECONDS);
        int tryTimes = 2;
        boolean isSuccess = false;
        for (int i = 0; i <= tryTimes; ++i) {
            try {
                link(pageRequest.getUrl());
                isSuccess = true;
                break;
            } catch (Exception e) {
                continue;
            }
        }
        ((RemoteWebDriver)webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
        if (!isSuccess) {
            isSkip = true;
        }
          }

    @Override
    protected void internalClose() {
        webDriver.close();
    }

    @Override
    protected String rawContent() {
        return webDriver.getPageSource();
    }

    @Override
    protected int responseCode() {
        return 200;
    }

    @Override
    protected Map<String, List<String>> responseHeaders() {
        return null;
    }

    @Override
    protected void internalReset() {
        webDriver.manage().deleteAllCookies();
    }

    @Override
    public DynamicDownload input(String inputTextElementQuery, String value) {
        if (isSkip) {
            return this;
        }
        webDriver.findElement(By.cssSelector(inputTextElementQuery)).sendKeys(value);
        return this;
    }

    @Override
    public DynamicDownload inputSubmit(String submitElementQuery) {
        if (isSkip) {
            return this;
        }
        webDriver.findElement(By.cssSelector(submitElementQuery)).submit();
        return this;
    }

    @Override
    public DynamicDownload click(String enableClickElementQuery) {
        if (isSkip) {
            return this;
        }
        boolean clicked = false;
        for (int retryTime = 0; retryTime < 2; ++retryTime) {
            List<WebElement> elements = webDriver.findElements(By.cssSelector(enableClickElementQuery));
            if (!elements.isEmpty()) {
                elements.get(0).click();
                clicked = true;
                break;
            } else {
                waitUtilShow(enableClickElementQuery, 1, TimeUnit.SECONDS);
                retryTime++;
            }
        }
        if (!clicked) {
            isSkip = true;
        }
        return this;
    }


    @Override
    public DynamicDownload waitUtilShow(String elementQuery, long time, TimeUnit timeUnit) {
        try {
            ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elementQuery));
            Function<WebDriver, WebElement> function = webDriver1 -> condition.apply(webDriver1);
            new WebDriverWait(webDriver, timeUnit.toSeconds(time))
                    .until(condition);
        } catch (TimeoutException ex) {
            isSkip = true;
        }
        return this;
    }

    @Override
    public DynamicDownload linkTo(String url) {
        webDriver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
        int tryTimes = 2;
        boolean isSuccess = false;
        for (int i = 0; i <= tryTimes; ++i) {
            try {
                link(url);
                isSuccess = true;
                break;
            } catch (Exception e) {
                continue;
            }
        }
        if (!isSuccess) {
            isSkip = true;
        }
        return this;
    }

    private void link(String url) {
        webDriver.get(url);
    }

    public static class Builder extends AbstractBuilder {
        private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();



        @Override
        protected AbstractDownLoad internalBuild() {
            try {
                desiredCapabilities.setJavascriptEnabled(true);
                PhantomJSDriver phantomJSDriver = new PhantomJSDriver(desiredCapabilities);
                SeleniumDownload seleniumDownload = new SeleniumDownload(phantomJSDriver);
                return seleniumDownload;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected AbstractBuilder internalProxy(String proxyHost, int proxyPort) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyHost + ":" + proxyPort);
            desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
            return this;
        }
    }
}
