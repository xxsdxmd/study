package com.example.application.iface.user.factory;

import com.example.opstudycommon.enums.EnumFilterSelectorScene;
import com.example.opstudycommon.filter.selector.FilterSelector;
import com.example.opstudycommon.filter.selector.LocalListBasedFilterSelector;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author xxs
 * @Date 2024/7/2 22:43
 * 用户过滤器选择器工厂实现
 */
@Service
public class UserFilterSelectorFactoryImpl implements FilterSelectorFactory {

    @Override
    public FilterSelector getFilterSelector(EnumFilterSelectorScene selectorScene) {
        LocalListBasedFilterSelector selector = new LocalListBasedFilterSelector();
        
        switch (selectorScene) {
            case USER:
                // 通用用户过滤器
                selector.addFilters(Arrays.asList(
                    "userValidationFilter",
                    "userDuplicateCheckFilter"
                ));
                break;
            case REGISTER:
                // 用户注册过滤器
                selector.addFilters(Arrays.asList(
                    "userValidationFilter",
                    "userDuplicateCheckFilter",
                    "userMobilePhoneFilter"
                ));
                break;
            case LOGIN:
                // 用户登录过滤器
                selector.addFilters(Arrays.asList(
                    "userValidationFilter",
                    "userLoginFilter"
                ));
                break;
            case UPDATE:
                // 用户更新过滤器
                selector.addFilters(Arrays.asList(
                    "userValidationFilter",
                    "userUpdateFilter"
                ));
                break;
            case QUERY:
                // 用户查询过滤器
                selector.addFilters(Collections.singletonList(
                        "userQueryFilter"
                ));
                break;
            default:
                // 默认返回空的过滤器选择器
                break;
        }
        
        return selector;
    }
}
