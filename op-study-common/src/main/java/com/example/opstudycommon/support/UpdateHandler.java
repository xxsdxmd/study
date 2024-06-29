package com.example.opstudycommon.support;

import domain.iface.Aggregate;
import domain.iface.Identifier;

import java.util.function.Consumer;

/**
 * @author xxs
 * @Date 2024/6/29 22:49
 */
public interface UpdateHandler<T extends Aggregate<ID>,ID extends Identifier> {

    /**
     * update -> executor
     * @param supplier
     * @return
     */
    Executor<T> update(Consumer<T> supplier);
}
