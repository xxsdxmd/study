package com.example.opstudycommon.filter.context;

import com.example.opstudycommon.filter.selector.FilterSelector;

/**
 * @author xxs
 * @Date 2024/7/2 22:31
 */
public abstract class AbstractContext implements Context {

    private final FilterSelector selector;

    public AbstractContext(FilterSelector selector) {
        this.selector = selector;
    }

    @Override
    public FilterSelector getFilterSelector() {
        return this.selector;
    }

}
