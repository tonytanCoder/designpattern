package com.tan.designpattern.creation.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 懒汉式
 * 懒汉式相对于饿汉式的优势是支持延迟加载。这种实现方式会导致频繁加锁、释放锁，以及并发度低等问题，频繁的调用会产生性能瓶颈。
 */
public class IdGenerator_Lazy {

    private AtomicLong id = new AtomicLong(0);

    private static IdGenerator_Lazy instance;

    private IdGenerator_Lazy() {
    }

    public static synchronized IdGenerator_Lazy getInstance() {
        if (instance == null) {
            instance = new IdGenerator_Lazy();
        }
        return instance;
    }

    public long getId() {
        return id.incrementAndGet();
    }
}
