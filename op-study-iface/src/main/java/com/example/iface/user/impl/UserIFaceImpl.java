package com.example.iface.user.impl;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.iface.user.UserIFace;
import com.example.opstudycommon.result.Result;
import org.springframework.stereotype.Component;

/**
 * @author xxs
 * @Date 2024/6/30 21:08
 */
@Component
public class UserIFaceImpl implements UserIFace {

    private final UserService userService;

    public UserIFaceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Result<?> registerUser(UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }
}
