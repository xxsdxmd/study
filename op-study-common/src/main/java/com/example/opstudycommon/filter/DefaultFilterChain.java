package com.example.opstudycommon.filter;

import com.example.opstudycommon.filter.context.Context;

import java.util.Objects;

/**
 * @author xxs
 * @Date 2024/7/2 23:01
 */
public class DefaultFilterChain<T extends Context> implements FilterChain<T> {

    private FilterChain<T> next;
    private Filter<T> filter;

    public DefaultFilterChain(FilterChain<T> next,  Filter<T> filter) {
        this.next = next;
        this.filter = filter;
    }

    @Override
    public void handler(T context) {
        this.filter.filter(context, this);
    }

    @Override
    public void fireNext(T context) {
        FilterChain nextChain = this.next;
        if (Objects.nonNull(nextChain)) {
            nextChain.handler(context);
        }
    }
}
