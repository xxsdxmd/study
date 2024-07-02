package com.example.opstudycommon.filter.selector;

import java.util.List;

/**
 * @author xxs
 * @Date 2024/7/2 22:23
 */
public interface FilterSelector {

    /**
     * matchFilter
     * @param filterNames
     * @return
     */
    boolean matchFilter(String filterNames);

    /**
     * filters
     * @return
     */
    List<String> getFilterNames();
}
