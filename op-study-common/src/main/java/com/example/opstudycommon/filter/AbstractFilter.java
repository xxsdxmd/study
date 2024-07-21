package com.example.opstudycommon.filter;

import com.example.opstudycommon.filter.context.Context;

/**
 * @author xxs
 * @Date 2024/7/2 22:48
 */
public abstract class AbstractFilter<T extends Context> implements Filter<T> {

    @Override
    public void filter(T context, FilterChain<T> filterChain) {
        if (context.getFilterSelector().matchFilter(this.getClass().getSimpleName())) {
            this.handle(context);
        }

        if (context.continueChain()) {
            filterChain.fireNext(context);
        }
    }

    protected abstract void handle(T var1);
}
