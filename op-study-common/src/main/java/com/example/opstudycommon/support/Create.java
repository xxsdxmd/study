package com.example.opstudycommon.support;

import domain.iface.Aggregate;
import domain.iface.Identifier;

import java.util.function.Supplier;

/**
 * @author xxs
 * @Date 2024/6/29 22:50
 */
public interface Create<T extends Aggregate<ID>, ID extends Identifier> {


    /**
     * create -> update
     * @param supplier
     * @return
     */
    UpdateHandler<T, ID> create(Supplier<T> supplier);
}
