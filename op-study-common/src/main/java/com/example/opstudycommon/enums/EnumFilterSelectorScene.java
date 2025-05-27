package com.example.opstudycommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xxs
 * @Date 2024/7/2 22:41
 * 过滤器选择器场景枚举
 */
@Getter
@AllArgsConstructor
public enum EnumFilterSelectorScene {
    USER("用户相关过滤器"),
    REGISTER("用户注册过滤器"),
    LOGIN("用户登录过滤器"),
    UPDATE("用户更新过滤器"),
    QUERY("用户查询过滤器");

    private final String description;
}
