package com.tan.designpattern.creation.factory.method;

import java.util.HashMap;
import java.util.Map;

public class RuleConfigParserFactoryMap {
    private static final Map cachedFactories = new HashMap<>();

    static {
        cachedFactories.put("json", new JsonRuleConfigParserFactory());
        cachedFactories.put("xml", new XmlRuleConfigParserFactory());
        cachedFactories.put("yaml", new YamlRuleConfigParserFactory());
        cachedFactories.put("properties", new PropertiesRuleConfigParserFactory());
    }

    public static IRuleConfigParserFactory getParserFactory(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        IRuleConfigParserFactory parserFactory = (IRuleConfigParserFactory) cachedFactories.get(type.toLowerCase());
        return parserFactory;
    }
}
