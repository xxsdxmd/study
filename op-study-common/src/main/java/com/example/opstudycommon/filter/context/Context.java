package com.example.opstudycommon.filter.context;

import com.example.opstudycommon.filter.selector.FilterSelector;

/**
 * @author xxs
 * @Date 2024/7/2 22:23
 * 上下文 1. 只需要做两个事情 获取selector 2. 要不要继续执行filter
 */
public interface Context {

    FilterSelector getFilterSelector();

    boolean continueChain();
    
    /**
     * 获取上下文中的对象
     * @param key 键
     * @return 值
     */
    Object get(String key);
}
