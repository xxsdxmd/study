package com.example.opstudycommon.validator;

import lombok.Getter;

import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/29 23:08
 */
public class ValidationException extends RuntimeException {

    @Getter
    private final List<ValidateResult> results;

    public ValidationException(List<ValidateResult> results) {
        super();
        this.results = results;
    }
}
