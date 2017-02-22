package com.incra.sparkui.functional;

/**
 * Functional Interface which wraps any exception in  a runtime exception
 *
 * Useful for if you want to allow your lambda to throw exceptions which will get caught in parent method
 */
@FunctionalInterface
public interface UncheckedFunction<T, R> extends java.util.function.Function<T, R> {
    @Override
    default R apply(T t) {
        try {
            return applyOrThrow(t);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    R applyOrThrow(T t) throws Exception ;
}

