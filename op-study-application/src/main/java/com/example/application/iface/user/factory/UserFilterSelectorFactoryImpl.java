package com.example.application.iface.user.factory;

import com.example.application.iface.user.filter.UserMobilePhoneFilter;
import com.example.opstudycommon.enums.EnumFilterSelectorScene;
import com.example.opstudycommon.filter.selector.FilterSelector;
import com.example.opstudycommon.filter.selector.LocalListBasedFilterSelector;
import org.springframework.stereotype.Service;

/**
 * @author xxs
 * @Date 2024/7/2 22:43
 */
@Service
public class UserFilterSelectorFactoryImpl implements FilterSelectorFactory {

    @Override
    public FilterSelector getFilterSelector(EnumFilterSelectorScene selectorScene) {
        if (!EnumFilterSelectorScene.USER.equals(selectorScene)) {
            return null;
        }
        LocalListBasedFilterSelector selector = new LocalListBasedFilterSelector();
        selector.addFilter(UserMobilePhoneFilter.class.getSimpleName());
        return selector;
    }
}
