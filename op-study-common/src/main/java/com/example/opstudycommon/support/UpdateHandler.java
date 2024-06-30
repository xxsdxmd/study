package com.example.opstudycommon.support;

import java.util.function.Consumer;

/**
 * @author xxs
 * @Date 2024/6/29 22:49
 */
public interface UpdateHandler<T> {

    /**
     * update -> executor
     * @param supplier
     * @return
     */
    Executor<T> update(Consumer<T> supplier);
}
