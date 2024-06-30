package com.example.application.iface.user.impl;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.opstudycommon.result.Result;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @author xxs
 * @Date 2024/6/30 21:02
 * application层的逻辑
 * 主要做流程编排
 */
@Service
public class UserServiceImpl implements UserService {


    @Override
    public Result<?> registerUser(@Valid UserRequest userRequest) {

        return null;
    }
}
