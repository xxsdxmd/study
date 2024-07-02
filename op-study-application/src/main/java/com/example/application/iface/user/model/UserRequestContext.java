package com.example.application.iface.user.model;

import com.example.application.iface.model.UserRequest;
import com.example.opstudycommon.filter.context.AbstractContext;
import com.example.opstudycommon.filter.selector.FilterSelector;
import lombok.*;

/**
 * @author xxs
 * @Date 2024/7/2 22:35
 * 上下文
 */
@Builder
public class UserRequestContext extends AbstractContext  {

    @Getter
    @Setter
    private FilterSelector filterSelector;

    @Getter
    @Setter
    private UserRequest userRequest;


    public UserRequestContext(FilterSelector filterSelector) {
        super(filterSelector);
    }

    @Override
    public boolean continueChain() {
        return true;
    }
}
