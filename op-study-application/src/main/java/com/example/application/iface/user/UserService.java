package com.example.application.iface.user;

import com.example.application.iface.model.UserRequest;
import com.example.opstudycommon.result.Result;

import javax.validation.Valid;

/**
 * @author xxs
 * @Date 2024/6/30 21:00
 */
public interface UserService {

    /**
     * 用户注册
     */
    Result<?> registerUser(@Valid UserRequest userRequest);
}
