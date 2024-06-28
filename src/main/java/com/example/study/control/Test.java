package com.example.study.control;

import com.example.study.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxs
 * @Date 2024/6/28 0:28
 */
@RestController
@RequestMapping("/test")
public class Test {


    @PostMapping("/find")
    public Result<Integer> find() {
        return Result.<Integer>builder()
                .code(200)
                .msg("成功")
                .data(123)
                .build();
    }

}
