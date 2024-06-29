package com.example.opstudycommon.support;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author xxs
 * @Date 2024/6/29 22:49
 */
public interface Executor<T> {

    /**
     * executor
     * @return
     */
    Optional<T> executor();

    Executor<T> successHook(Consumer<T> consumer);

    Executor<T> errorHook(Consumer<? super Throwable> consumer);
}
