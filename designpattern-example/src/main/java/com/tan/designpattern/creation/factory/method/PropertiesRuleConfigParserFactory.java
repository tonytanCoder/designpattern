package com.tan.designpattern.creation.factory.method;

import com.tan.designpattern.creation.factory.IRuleConfigParser;

public class PropertiesRuleConfigParserFactory implements IRuleConfigParserFactory{
    @Override
    public IRuleConfigParser createParser() {
        return new PropertiesRuleConfigParser();
    }
}