package com.example.application.iface.user.impl;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.application.iface.user.model.UserRequestContext;
import com.example.opstudycommon.enums.EnumFilterSelectorScene;
import com.example.application.iface.user.factory.FilterSelectorFactory;
import com.example.opstudycommon.filter.selector.FilterSelector;
import com.example.opstudycommon.result.Result;
import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.service.user.iface.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @author xxs
 * @Date 2024/6/30 21:02
 * application层的逻辑
 * 主要做流程编排
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private FilterSelectorFactory factory;

    private UserDomainService userDomainService;

    @Override
    public Result<?> registerUser(@Valid UserRequest userRequest) {
        UserEntity userEntity = userDomainService.selectById(new UserId(userRequest.getId()));
        FilterSelector userFilterSelector = factory.getFilterSelector(EnumFilterSelectorScene.USER);
        UserRequestContext userRequestContext = buildUserRequestContext(userRequest,userFilterSelector);
        return Result.success();
    }


    /**
     * build context
     * @param userRequest
     * @param selector
     * @return
     */
    private UserRequestContext buildUserRequestContext(UserRequest userRequest,FilterSelector selector) {
        return UserRequestContext.builder()
                .filterSelector(selector)
                .userRequest(userRequest)
                .build();
    }
}
