package com.incra.sparkui.functional;

/**
 * Functional Interface which wraps any exception in  a runtime exception
 *
 * Useful for if you want to allow your lambda to throw exceptions which will get caught in parent method
 */
public interface UncheckedRunnable extends Runnable {
    @Override
    default void run() {
        try {
            runOrThrow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void runOrThrow() throws Exception;
}
