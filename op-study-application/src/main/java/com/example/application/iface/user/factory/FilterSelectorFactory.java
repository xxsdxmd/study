package com.example.application.iface.user.factory;

import com.example.opstudycommon.enums.EnumFilterSelectorScene;
import com.example.opstudycommon.filter.selector.FilterSelector;

/**
 * @author xxs
 * @Date 2024/7/2 22:40
 */
public interface FilterSelectorFactory {

    /**
     * 获取select
     * @param selectorScene
     * @return
     */
    FilterSelector getFilterSelector(EnumFilterSelectorScene selectorScene);
}
