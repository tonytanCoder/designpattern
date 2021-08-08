package com.tan.designpattern.creation.factory.method;

import com.tan.designpattern.creation.factory.IRuleConfigParser;

public interface IRuleConfigParserFactory {
    IRuleConfigParser createParser();
}
