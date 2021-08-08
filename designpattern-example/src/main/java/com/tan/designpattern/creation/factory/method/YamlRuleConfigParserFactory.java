package com.tan.designpattern.creation.factory.method;

import com.tan.designpattern.creation.factory.IRuleConfigParser;

public class YamlRuleConfigParserFactory implements IRuleConfigParserFactory {
    @Override
    public IRuleConfigParser createParser() {
        return new YamlRuleConfigParser();
    }
}
