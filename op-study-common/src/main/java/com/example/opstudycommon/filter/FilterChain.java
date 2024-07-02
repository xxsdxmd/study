package com.example.opstudycommon.filter;

/**
 * @author xxs
 * @Date 2024/7/2 22:19
 */
public interface FilterChain<T>  {

    void handler(T context);

    void fireNext(T context);
}
