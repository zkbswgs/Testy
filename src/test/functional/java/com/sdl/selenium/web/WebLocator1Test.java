package com.sdl.selenium.web;

import com.sdl.weblocator.InputData;
import com.sdl.weblocator.TestBase;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class WebLocator1Test extends TestBase {
    private static final Logger logger = Logger.getLogger(WebLocator1Test.class);

    WebLocator webLocatorId = new WebLocator().setId("webLocatorId");
    WebLocator webLocatorCls = new WebLocator().setClasses("webLocatorCls");
    WebLocator webLocatorNotAttribute = new WebLocator().setClasses("notExist");

    WebLocator webLocatorWithMoreEnter = new WebLocator().setClasses("more-elements-inside").setText("more enter inside div");
    WebLocator webLocatorWithMoreEnterMoreElements = new WebLocator().setClasses("more-elements-inside").setText("more enter inside div", SearchType.DEEP_CHILD_NODE);
    WebLocator webLocatorNoWithMoreEnterMoreElements = new WebLocator().setClasses("more-elements-inside no-enter").setText("more enter inside div", SearchType.DEEP_CHILD_NODE);
    WebLocator webLocatorNoWithMoreEnter = new WebLocator().setClasses("more-elements-inside no-enter").setText("more enter inside div");
    WebLocator webLocatorWithMoreText = new WebLocator().setElPath("//*[contains(@class, 'element7') and concat(text()[1], ./*/text(), text()[2], ./*/text()[contains(.,'care')], text()[3])='Some important text care trebuie']");
    WebLocator webLocatorComplex = new WebLocator().setClasses("element11").setText("Some more important text that is very important . end", SearchType.HTML_NODE);
    WebLocator webLocatorLogger = new WebLocator().setId("logger");


    @BeforeClass
    public void startTests() {
        driver.get(InputData.WEB_LOCATOR_URL);
    }

    @Test
    public void identifyById() {
        assertEquals(webLocatorId.getAttributeId(), "webLocatorId");
        assertEquals(webLocatorId.getAttributeClass(), "");
    }

    @Test
    public void identifyByClass() {
        assertEquals(webLocatorCls.getAttributeClass(), "webLocatorCls");
        assertEquals(webLocatorCls.getAttributeId(), "");
    }

    @Test
    public void attributeForNotFoundElement() {
        assertEquals(webLocatorNotAttribute.getAttributeClass(), null);
    }

    @Test
    public void webDriverConfig() {
        WebLocator l = new WebLocator().setClasses("x-tool-maximize");
        logger.debug(l.getPath());
        logger.debug("//*[contains(@class,'x-tool-maximize')]");

        final String browser = InputData.BROWSER;
        if("*firefox".equalsIgnoreCase(browser)){
            assertTrue(WebDriverConfig.isFireFox());
        } else if("*chrome".equalsIgnoreCase(browser)){
            assertTrue(WebDriverConfig.isChrome());
        } else if("*iexplore".equalsIgnoreCase(browser)){
            assertTrue(WebDriverConfig.isIE());
        }
    }

    @Test
    public void getHtmlTextFromDiv() {
        assertEquals(webLocatorWithMoreEnter.getAttributeClass(), "more-elements-inside no-enter element4");
        assertEquals(webLocatorWithMoreEnterMoreElements.getAttributeClass(), "more-elements-inside with-enter element1");
        assertEquals(webLocatorNoWithMoreEnterMoreElements.getAttributeClass(), "more-elements-inside no-enter element3");
        assertEquals(webLocatorNoWithMoreEnter.getAttributeClass(), "more-elements-inside no-enter element4");
        assertEquals(webLocatorWithMoreText.getAttributeClass(), "more-elements-inside with-enter element7");
        assertEquals(webLocatorComplex.getAttributeClass(), "more-elements-inside with-enter element11");
    }

    @Test (dataProviderClass = WebLocatorTest.class, dataProvider = "testConstructorPathDataProviderText" )
    public void shouldFindAllCombinationsForTextSearchTypeTest(WebLocator el, String expectedPath) {

        logger.debug(el.getPath());
        assertTrue(el.click());

        boolean useChildNodesSearch = el.getSearchTextType().contains(SearchType.DEEP_CHILD_NODE);

        String expected = "WebLocator text for search type-searchTextType" + (useChildNodesSearch ? " deep" : "");
        assertEquals(webLocatorLogger.getHtmlText(), expected);

        webLocatorWithMoreEnter.click();
    }
}
