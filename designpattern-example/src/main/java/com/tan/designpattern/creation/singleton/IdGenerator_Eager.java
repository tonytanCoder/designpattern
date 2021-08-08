package com.tan.designpattern.creation.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 饿汉式
 * 饿汉式的实现方式，在类加载的期间，就已经将 instance 静态实例初始化好了，所以，instance 实例的创建是线程安全的。不过，这样的实现方式不支持延迟加载实例。
 */
public class IdGenerator_Eager {

    private AtomicLong id = new AtomicLong(0);

    private static final IdGenerator_Eager instance = new IdGenerator_Eager();

    private IdGenerator_Eager() {
    }

    public static IdGenerator_Eager getInstance() {
        return instance;
    }

    public long getId() {
        return id.incrementAndGet();
    }
}
