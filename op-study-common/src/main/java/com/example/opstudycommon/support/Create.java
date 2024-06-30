package com.example.opstudycommon.support;


import java.util.function.Supplier;

/**
 * @author xxs
 * @Date 2024/6/29 22:50
 */
public interface Create<T> {


    /**
     * create -> update
     * @param supplier
     * @return
     */
    UpdateHandler<T> create(Supplier<T> supplier);
}
