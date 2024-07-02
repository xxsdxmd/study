package com.example.opstudycommon.filter.selector;

import java.util.List;

/**
 * @author xxs
 * @Date 2024/7/2 22:26
 */
public class DummyFilterSelector implements FilterSelector {

    // 可以读阿波罗 把filters 配置好 读一个List

    @Override
    public boolean matchFilter(String filterNames) {
        return false;
    }

    @Override
    public List<String> getFilterNames() {
        return null;
    }
}
