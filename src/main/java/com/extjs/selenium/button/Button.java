package com.extjs.selenium.button;

import com.extjs.selenium.ExtJsComponent;
import com.extjs.selenium.Utils;
import com.sdl.selenium.web.SearchType;
import com.sdl.selenium.web.WebDriverConfig;
import com.sdl.selenium.web.WebLocator;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

public class Button extends ExtJsComponent {
    private static final Logger logger = Logger.getLogger(Button.class);

    public String getIconCls() {
        return iconCls;
    }

    public <T extends Button> T setIconCls(final String iconCls) {
        this.iconCls = iconCls;
        return (T) this;
    }

    private String iconCls;

    public Boolean hasIconCls() {
        return iconCls != null && !iconCls.equals("");
    }

    public Button() {
        setClassName("Button");
        setBaseCls("x-btn");
        setTag("table");
        setVisibility(true);
        defaultSearchTextType.add(SearchType.DEEP_CHILD_NODE);
    }

    /**
     * @param cls
     * @deprecated
     */
    public Button(String cls) {
        this();
        setClasses(cls);
    }

    /**
     * @param container
     */
    public Button(WebLocator container) {
        this();
        setContainer(container);
    }

    public Button(WebLocator container, String text) {
        this(container);
        setText(text, SearchType.EQUALS);
    }

    // Methods

    @Override
    protected String getItemPathText() {
        String selector = hasText() ? super.getItemPathText() : "";
        if (hasIconCls()) {
            selector += " and count(.//*[contains(@class, '" + getIconCls() + "')]) > 0";
        }
        return selector;
    }

    @Override
    public String getItemPath(boolean disabled) {
        // TODO create iconCls & iconPath for buttons with no text
        String selector = getBasePathSelector();
        if (!disabled) {
            selector += " and not(contains(@class, 'x-item-disabled'))";
        }
        selector = Utils.fixPathSelector(selector);
        return "//" + getTag() + "[" + selector + "]";
    }

    @Override
    public boolean click() {
        // to scroll to this element (if element is not visible)
        WebLocator buttonEl = new WebLocator(this, "//button").setInfoMessage(this.toString() + "//button");
        // TODO try to click on button that has mask - with first solution is not saying that has mask
        //ExtJsComponent buttonEl = new ExtJsComponent(this, "//button").setInfoMessage(this + "//button");
        buttonEl.setRenderMillis(getRenderMillis());
        boolean buttonExist = true;
        if (WebDriverConfig.hasWebDriver()) {
            buttonEl.sendKeys(Keys.TAB);
            buttonExist = buttonEl.currentElement != null;
        } else {
            buttonEl.focus();
        }
        boolean clicked = buttonExist && isElementPresent() && super.doClick();
        if (clicked) {
            Utils.sleep(50);
        } else {
            logger.error("(" + toString() + ") doesn't exists or is disabled. " + getPath());
        }
        return clicked;
    }

    public boolean toggle(boolean state) {
        boolean toggled = false;
        if (ready()) {
            String cls = getAttributeClass();
            boolean contains = cls.contains("x-btn-pressed");
            if (state && !contains || !state && contains) {
                toggled = click();
            } else {
                toggled = true;
            }
        }
        return toggled;
    }

    /**
     * TO Be used in extreme cases when simple .click is not working
     *
     * @return
     */
    public boolean clickWithExtJS() {
        String id = getAttributeId();
        String script = "return (function(){var b = Ext.getCmp('" + id + "'); if(b) {b.handler.call(b.scope || b, b); return true;} return false;})()";
//        logger.debug("clickWithExtJS: "+ script);
        Object object = executeScript(script);
        logger.debug("clickWithExtJS result: " + object);
        return (Boolean) object;
    }

    /**
     * Using XPath only
     *
     * @return
     */
    public boolean isDisabled() {
        return new WebLocator(null, getPath(true)).exists();
    }

    /**
     * @param milliseconds
     * @return
     */
    public boolean waitToEnable(long milliseconds) {
        return waitToRender(milliseconds);
    }

    /**
     * @return
     */
    public boolean isEnabled() {
        return exists();
    }

    /**
     * @return
     */
    public boolean showMenu() {
        // TODO try to find solution without runScript
        final String id = getAttributeId();
        if (id != null && !id.equals("")) {
            String script = "Ext.getCmp('" + id + "').showMenu()";
            Boolean showMenu = (Boolean) executeScript(script);
            Utils.sleep(200);
            return showMenu;
        }
        return false;
    }

    /**
     * new String[]{"option1", "option2", "option3-click"}
     *
     * @param menuOptions
     * @return
     */
    public boolean clickOnMenu(String[] menuOptions) {
        logger.debug("clickOnMenu : " + menuOptions[menuOptions.length - 1]);
        if (click()) {
            String info = toString();
//            logger.info("Click on button " + info);
            // TODO try to use Menu class for implementing select item
            WebLocator menu = new WebLocator("x-menu-floating");
            if (WebDriverConfig.isIE()) {
                // menu.isVisible is not considered but is executed and is just consuming time.
//                if(menu.isVisible()){
//                    logger.info("In IE is visible");
//                }
            } else {
                menu.setStyle("visibility: visible;");
            }
            menu.setInfoMessage("active menu");
            ExtJsComponent option = new ExtJsComponent(menu);
            for (String menuOption : menuOptions) {
                option.setText(menuOption);
                if (!option.mouseOver()) {
                    return false;
                }
            }
            if (option.clickAt()) {
                return true;
            } else {
                logger.warn("Could not locate option '" + option.getText() + "'. Performing simple click on button : " + info);
                doClickAt();
            }
        }
        return false;
    }


    /**
     * @param option
     * @return
     */
    public boolean clickOnMenu(String option) {
        return clickOnMenu(new String[]{option});
    }
}