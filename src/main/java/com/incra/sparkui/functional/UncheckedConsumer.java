package com.incra.sparkui.functional;

/**
 * Functional Interface which wraps any exception in  a runtime exception
 *
 * Useful for if you want to allow your lambda to throw exceptions which will get caught in parent method
 */
@FunctionalInterface
public interface UncheckedConsumer<T> extends java.util.function.Consumer<T> {

    @Override
    default void accept(final T elem) {
        try {
            acceptOrThrow(elem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void acceptOrThrow(T elem) throws Exception;

}

