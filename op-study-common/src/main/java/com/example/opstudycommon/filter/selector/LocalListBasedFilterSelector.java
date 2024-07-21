package com.example.opstudycommon.filter.selector;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

/**
 * @author xxs
 * @Date 2024/7/2 22:24
 * 静态的 filters 由代码写死
 */
public class LocalListBasedFilterSelector implements FilterSelector {

    private final List<String> filterNameLists = Lists.newArrayList();
    @Override
    public boolean matchFilter(String filterNames) {
        return filterNameLists.stream().anyMatch(data -> Objects.equals(data, filterNames));
    }

    @Override
    public List<String> getFilterNames() {
        return filterNameLists;
    }

    /**
     * 添加filter
     * @param clsNames
     */
    public void addFilter(String clsNames) {
        this.filterNameLists.add(clsNames);
    }
}
