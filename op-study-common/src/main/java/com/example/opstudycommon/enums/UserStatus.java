package com.example.opstudycommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户状态枚举
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活"),
    DISABLED(-1, "已禁用"),
    DELETED(-2, "已删除");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static UserStatus getByCode(Integer code) {
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为有效状态
     */
    public boolean isValid() {
        return this == ACTIVE || this == INACTIVE;
    }
} 