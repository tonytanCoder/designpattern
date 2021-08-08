package com.tan.designpattern.creation.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 静态内部类
 * 利用 Java 的静态内部类来实现单例。这种实现方式，既支持延迟加载，也支持高并发，实现起来也比双重检测简单。
 */
public class IdGenerator_innerstatic {

    private AtomicLong id = new AtomicLong(0);

    private IdGenerator_innerstatic() {
    }

    private static class SingletonHolder {
        private static final IdGenerator_innerstatic instance = new IdGenerator_innerstatic();
    }

    public static IdGenerator_innerstatic getInstance() {
        return SingletonHolder.instance;
    }

    public long getId() {
        return id.incrementAndGet();
    }
}
