package com.example.iface.user;

import com.example.application.iface.model.UserRequest;
import com.example.opstudycommon.result.Result;

/**
 * @author xxs
 * @Date 2024/6/28 22:10
 */
public interface UserIFace {

    /**
     * 用户注册
     */
    Result<?> registerUser(UserRequest userRequest);

}
