package com.example.opstudycommon.filter;

import com.example.opstudycommon.filter.context.Context;

/**
 * @author xxs
 * @Date 2024/6/30 0:22
 * 通用filter的封装
 */
public interface Filter<T extends Context> {


    void filter(T context, FilterChain filterChain);
}
