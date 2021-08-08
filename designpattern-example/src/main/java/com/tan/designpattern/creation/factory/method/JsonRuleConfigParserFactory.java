package com.tan.designpattern.creation.factory.method;

import com.tan.designpattern.creation.factory.IRuleConfigParser;

public class JsonRuleConfigParserFactory implements IRuleConfigParserFactory{
    @Override
    public IRuleConfigParser createParser() {
        return new JsonRuleConfigParser();
    }
}
