package com.extjs.selenium.conditions;

import com.extjs.selenium.window.MessageBox;
import com.sdl.selenium.conditions.FailCondition;
import com.sdl.selenium.conditions.MessageBoxCondition;
import org.apache.log4j.Logger;

public class MessageBoxFailCondition extends FailCondition implements MessageBoxCondition {
    private static final Logger logger = Logger.getLogger(MessageBoxFailCondition.class);
    private boolean contains = false;

    public MessageBoxFailCondition(String message) {
        super(message);
    }

    public MessageBoxFailCondition(String message, int priority) {
        this(message);
        setPriority(priority);
    }

    public MessageBoxFailCondition(String message, boolean contains) {
        this(message);
        this.contains = contains;
    }

    public MessageBoxFailCondition(String message, boolean contains, int priority) {
        this(message, contains);
        setPriority(priority);
    }

    @Override
    public boolean execute() {
        return execute(MessageBox.getMessage());
    }


    @Override
    public boolean execute(final String boxMessage){
        boolean executed = false;
        if (contains) {
            if (boxMessage != null && boxMessage.contains(getMessage())) {
                executed = true;
            }
        } else if (getMessage().equals(boxMessage)) {
            executed = true;
        }
        if (executed) {
            this.setResultMessage(boxMessage);
        }
        return executed;
    }

    public String toString() {
        return super.toString() + getMessage();
    }
}