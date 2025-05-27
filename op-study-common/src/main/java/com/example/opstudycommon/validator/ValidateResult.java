package com.example.opstudycommon.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xxs
 * @Date 2024/6/29 23:06
 */
@Data
@AllArgsConstructor
public class ValidateResult {

    private String name;

    private String message;
}
