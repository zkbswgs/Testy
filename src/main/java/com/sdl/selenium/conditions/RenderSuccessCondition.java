package com.sdl.selenium.conditions;

import com.sdl.selenium.web.WebLocator;
import org.apache.log4j.Logger;

public class RenderSuccessCondition extends SuccessCondition implements RenderCondition {
    private static final Logger logger = Logger.getLogger(RenderSuccessCondition.class);

    private WebLocator component;

    private RenderSuccessCondition(String message) {
        super(message);
    }

    public RenderSuccessCondition(WebLocator component){
        this(component.toString());
        this.component = component;
    }

    public RenderSuccessCondition(WebLocator component, int priority){
        this(component);
        setPriority(priority);
    }

    @Override
    public boolean execute() {
        return component.isElementPresent();
    }

    @Override
    public String toString() {
        return super.toString() + component;
    }

    @Override
    public WebLocator getComponent(){
         return component;
    }

    /**
     *
     * @return component.getHtmlText()
     */
    @Override
    public String getResultMessage() {
        return component.getHtmlText();
    }
}
