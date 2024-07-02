package com.example.application.iface.user.factory;

import com.example.opstudycommon.filter.DefaultFilterChain;
import com.example.opstudycommon.filter.Filter;

/**
 * @author xxs
 * @Date 2024/7/2 23:10
 */
public class UserChainFactory<T extends Filter> {

    private DefaultFilterChain defaultFilterChain;


    public  DefaultFilterChain getDefaultFilterChain() {
        return this.defaultFilterChain;
    }


}
