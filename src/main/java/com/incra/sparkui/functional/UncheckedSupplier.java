package com.incra.sparkui.functional;

/**
 * Functional Interface which wraps any exception in  a runtime exception
 *
 * Useful for if you want to allow your lambda to throw exceptions which will get caught in parent method
 */
@FunctionalInterface
public interface UncheckedSupplier<T> extends java.util.function.Supplier<T> {
    @Override
    default T get() {
        try {
            return getOrThrow();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    T getOrThrow() throws Exception ;
}