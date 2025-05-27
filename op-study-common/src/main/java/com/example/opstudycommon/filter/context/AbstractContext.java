package com.example.opstudycommon.filter.context;

import com.example.opstudycommon.filter.selector.FilterSelector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xxs
 * @Date 2024/7/2 22:31
 */
public abstract class AbstractContext implements Context {

    private final FilterSelector selector;
    private final Map<String, Object> attributes = new HashMap<>();

    public AbstractContext(FilterSelector selector) {
        this.selector = selector;
    }

    @Override
    public FilterSelector getFilterSelector() {
        return this.selector;
    }

    @Override
    public Object get(String key) {
        return attributes.get(key);
    }
    
    /**
     * 设置属性
     */
    public void put(String key, Object value) {
        attributes.put(key, value);
    }
    
    /**
     * 检查是否包含属性
     */
    public boolean containsKey(String key) {
        return attributes.containsKey(key);
    }

}
