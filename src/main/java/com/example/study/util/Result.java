package com.example.study.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxs
 * @Date 2024/6/28 0:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;


}
