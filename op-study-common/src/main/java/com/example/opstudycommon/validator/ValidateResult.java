package com.example.opstudycommon.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * @author xxs
 * @Date 2024/6/29 23:06
 */
public class ValidateResult {

    String name;

    String message;

    public ValidateResult(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
