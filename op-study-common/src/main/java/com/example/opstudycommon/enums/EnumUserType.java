package com.example.opstudycommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xxs
 * @Date 2024/6/30 16:35
 * 用户操作类型枚举
 */
@Getter
@AllArgsConstructor
public enum EnumUserType {
    CREATE("创建用户"),
    UPDATE("更新用户"),
    DELETE("删除用户"),
    LOGIN("用户登录"),
    LOGOUT("用户登出"),
    REGISTER("用户注册"),
    ACTIVATE("激活用户"),
    DEACTIVATE("停用用户");

    private final String description;
}
