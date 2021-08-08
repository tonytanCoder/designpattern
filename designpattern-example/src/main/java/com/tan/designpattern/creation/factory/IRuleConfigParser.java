package com.tan.designpattern.creation.factory;

import com.tan.designpattern.creation.factory.method.RuleConfig;

public interface IRuleConfigParser {
    RuleConfig parse(String path);
}
