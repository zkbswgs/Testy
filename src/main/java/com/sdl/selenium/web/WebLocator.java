package com.sdl.selenium.web;

import com.extjs.selenium.Utils;
import com.thoughtworks.selenium.Selenium;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class WebLocator extends WebLocatorAbstractBuilder {
    private static final Logger logger = Logger.getLogger(WebLocator.class);

    public static WebDriver driver;
    public static Selenium selenium;

    private String currentElementPath = "";
    public WebElement currentElement;

    public static WebLocatorExecutor getExecutor() {
        return executor;
    }

    protected static WebLocatorExecutor executor;

    public WebLocator() {
    }

    /**
     * @param cls
     */
    public WebLocator(String cls) {
        setClasses(cls);
    }

    public WebLocator(WebLocator container) {
        setContainer(container);
    }

    public WebLocator(WebLocator container, String elPath) {
        this(container);
        setElPath(elPath);
    }

    public WebLocator(String cls, WebLocator container) {
        this(container);
        setClasses(cls);
    }

    public WebLocator(String text, String cls, WebLocator container) {
        this(cls, container);
        setText(text);
    }

    // getters and setters

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        WebLocator.driver = driver;
        executor = new WebLocatorDriverExecutor(driver);
        WebDriverConfig.init(driver);
    }

    public static Selenium getSelenium() {
        return selenium;
    }

    public static void setSelenium(Selenium selenium) {
        WebLocator.selenium = selenium;
        executor = new WebLocatorSeleniumExecutor(selenium);
    }

    /**
     * @deprecated use from WebDriverConfig.hasWebDriver()
     * @return
     */
    public static boolean hasWebDriver() {
        return WebDriverConfig.hasWebDriver();
    }

    /**
     * @return id
     */
    public String getAttributeId() {
        return getAttribute("id");
    }

    /**
     * @return class
     */
    public String getAttributeClass() {
        return getAttribute("class");
    }

    public String getCurrentElementPath() {
        return currentElementPath;
    }

    public void setCurrentElementPath(String currentElementPath) {
        this.currentElementPath = currentElementPath;
    }

    /**
     * Use xPath only
     *
     * @param attribute
     * @return String attribute, if element not exist return null.
     */
    public String getAttribute(String attribute) {
        return executor.getAttribute(this, attribute);
    }

    protected String getCurrentElementAttribute(String attribute){
        return executor.getCurrentElementAttribute(this, attribute);
    }

    public String getHtmlSource() {
        return executor.getHtmlSource(this);
    }

    /**
     * xPath only
     *
     * @return
     */
    public String getHtmlText() {
        return getHtmlText(false);
    }

    public String getHtmlText(boolean instant) {
        if(instant || ready()){
            return executor.getHtmlText(this);
        }
        return null;
    }

    /**
     * Using XPath only
     */
    public boolean clickAt() {
        boolean clickAt = ready() && doClickAt();
        if (clickAt) {
            logger.info("ClickAt on " + this);
        }
        return clickAt;
    }

    public boolean assertClickAt() {
        boolean clicked = clickAt();
        if (!clicked) {
            Assert.fail("Could not clickAt on: " + this);
        }
        return clicked;
    }

    public boolean assertExists() {
        boolean exists = exists();
        if (!exists) {
            Assert.fail("Element does not exists : " + this);
        }
        return exists;
    }

    /**
     * Use xPath only. Click once do you catch exceptions StaleElementReferenceException.
     *
     * @return
     */
    public boolean click() {
        boolean click = waitToRender() && doClick();
        if (click) {
            logger.info("Click on " + this);
        }
        return click;
    }

    /**
     * //TODO Daca nu a facut click pe element sa se intrerupa testul de facut si pentru clickAt() si sa fie setabil
     *
     * @return
     */

    public boolean assertClick() {
        boolean clicked = click();
        if (!clicked) {
            Assert.fail("Could not click on: " + this);
        }
        return clicked;
    }

    /**
     * doClick does not make sure element is present, if you are not sure about this, please use click() instead
     *
     * @return
     */
    protected boolean doClick() {
        return executor.doClick(this);
    }

    /**
     * doClickAt does not make sure element is present, if you are not sure about this, please use click() instead
     *
     * @return
     */
    protected boolean doClickAt() {
        return executor.doClickAt(this);
    }

    public void highlight() {
        if (isElementPresent()) {
            doHighlight();
        }
    }

    private void doHighlight() {
        executor.doHighlight(this);
    }

    public WebLocator sendKeys(java.lang.CharSequence... charSequences) {
        if(ready()){
            executor.doSendKeys(this, charSequences);
        }
        return this;
    }

    public boolean clear() {
        return executor.clear(this);
    }

    /**
     * Using XPath only
     *
     * @return
     */
    public boolean mouseOver() {
        if (ready()) {
            logger.info("mouseOver on " + this);
            return doMouseOver();
        } else {
            logger.warn("mouseOver on " + this + " failed");
            return false;
        }
    }

    protected boolean doMouseOver() {
        return executor.doMouseOver(this);
    }

    public boolean blur() {
        if (ready()) {
            logger.info("blur on " + this);
            executor.blur(this);
            return true;
        } else {
            logger.warn("blur on " + this + " failed");
            return false;
        }
    }

    /**
     * Using XPath only
     *
     * @return
     */
    public WebLocator focus() {
        if (ready()) {
            executor.focus(this);
            logger.info("focus on " + toString());
        }
        return this;
    }

    protected Object executeScript(String script, Object... objects) {
        return executor.executeScript(script, objects);
    }

    /**
     * Using XPath only
     *
     * @return
     */
    public boolean doubleClickAt() {
        boolean clicked = false;
        if (ready()) {
            clicked = executor.doubleClickAt(this);
        }
        return clicked;
    }

    /**
     * Using xPath only
     *
     * @return
     */
    public boolean isElementPresent() {
        return executor.isElementPresent(this);
    }

    public int size() {
        return executor.size(this);
    }

    public boolean exists() {
        return executor.exists(this);
    }

    public WebElement findElement() {
        return executor.findElement(this);
    }

    /**
     * Using XPath only
     *
     * @return
     */
    public boolean isVisible() {
        boolean visible = isElementPresent();
        if (visible) {
            String style = getAttribute("style");
            style = style == null ? "" : style.toLowerCase();
            style = style.replaceAll("\\s*:\\s*", ":");
            if (style.contains("visibility:hidden") || style.contains("display:none")) {
                visible = false;
            }
            /*else {
                visible = getContainer().isVisible();
                //TODO if must check parent is visible
            }*/
        }
        return visible;
    }

    public boolean isTextPresent(String text) {
        return executor.isTextPresent(this, text);
    }

    /**
     * wait 5 seconds (or specified value for renderSeconds)
     * Using xPath only.
     *
     * @return
     */
    public boolean waitToRender() {
        return waitToRender(getRenderMillis());
    }

    /**
     * Using xPath only
     *
     * @param seconds
     * @return
     */
    public boolean waitToRender(int seconds) {
        return waitToRender((long) seconds * 1000);
    }

    public boolean waitToRender(final long millis) {
        executor.waitElement(this, millis);
        return currentElement != null;
    }

    /**
     * @param seconds
     * @return
     */
    public String waitTextToRender(int seconds) {
        return waitTextToRender(seconds, "");
    }

    /**
     * Waits for the text to be loaded by looking at the content and not take in consideration the excludeText
     * text or what ever text is given as parameter
     *
     * @param seconds
     * @param excludeText
     * @return
     */
    public String waitTextToRender(int seconds, String excludeText) {
        String text = null;
        if (seconds == 0 && ((text = getHtmlText(true)) != null && text.length() > 0 && !text.equals(excludeText))) {
            return text;
        }
        for (int i = 0, count = 5 * seconds; i < count; i++) {
            text = getHtmlText(true);
            if (text != null && text.length() > 0 && !text.equals(excludeText)) {
                return text;
            }
            if (i == 0) {
                // log only fist time
                logger.debug("waitTextToRender");
            }
            Utils.sleep(200);
        }
        //logger.debug("No text was found for Element after " + seconds + " sec; " + path);
        logger.warn("No text was found for Element after " + seconds + " sec; " + this);
        return excludeText.equals(text) ? null : text;
    }

    public boolean waitToActivate() {
        return waitToActivate(getActivateSeconds());
    }

    /**
     * Wait for the element to be activated when there is deactivation mask on top of it
     *
     * @param seconds
     */
    public boolean waitToActivate(int seconds) {
        return true;
    }

    public boolean ready() {
        return waitToRender() && waitToActivate();
    }

    public boolean ready(int seconds) {
        return waitToRender((long) seconds * 1000) && waitToActivate(seconds);
    }
}