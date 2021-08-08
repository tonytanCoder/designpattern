package com.tan.designpattern.creation.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 双重检测
 * 双重检测实现方式既支持延迟加载、又支持高并发的单例实现方式。只要 instance 被创建之后，再调用 getInstance() 函数都不会进入到加锁逻辑中。所以，这种实现方式解决了懒汉式并发度低的问题。
 */
public class IdGenerator_Doublecheck {
    private AtomicLong id = new AtomicLong(0);
    private static IdGenerator_Doublecheck instance;

    private IdGenerator_Doublecheck() {
    }

    public static IdGenerator_Doublecheck getInstance() {
        if (instance == null) {
            synchronized (IdGenerator_Doublecheck.class) { // 此处为类级别的锁
                if (instance == null) {
                    instance = new IdGenerator_Doublecheck();
                }
            }
        }
        return instance;
    }

    public long getId() {
        return id.incrementAndGet();
    }
}
