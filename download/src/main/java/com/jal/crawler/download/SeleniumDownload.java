package com.jal.crawler.download;

import com.jal.crawler.request.PageRequest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        webDriver.get(pageRequest.getUrl());
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
        List<WebElement> elements = webDriver.findElements(By.cssSelector(enableClickElementQuery));
        if (!elements.isEmpty()) {
            elements.get(0).click();
        } else {
            isSkip = true;
        }
        return this;
    }

    @Override
    public DynamicDownload waitUtilShow(String elementQuery, long time, TimeUnit timeUnit) {
        try {
            new WebDriverWait(webDriver, timeUnit.toSeconds(time))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(elementQuery)));
        } catch (TimeoutException ex) {
            isSkip = true;
        }
        return this;
    }

    @Override
    public DynamicDownload linkTo(String url) {
        webDriver.get(url);
        return this;
    }

    public static class Builder extends AbstractBuilder {
        private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();


        @Override
        protected AbstractDownLoad internalBuild() {
            return new SeleniumDownload(new ChromeDriver(desiredCapabilities));
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
