package utils;

import java.util.concurrent.atomic.AtomicLong;

public class AccountNumberGenerator {
    private static final AtomicLong counter = new AtomicLong(100000);

    public static String generate() {
        return "ACC" + counter.incrementAndGet();
    }
}