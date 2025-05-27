package com.example.opstudycommon.filter.user;

import com.example.opstudycommon.filter.AbstractFilter;
import com.example.opstudycommon.filter.FilterChain;
import com.example.opstudycommon.filter.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户验证过滤器
 */
@Slf4j
@Component("userValidationFilter")
public class UserValidationFilter extends AbstractFilter<Context> {

    @Override
    public void filter(Context context, FilterChain<Context> filterChain) {
        log.info("执行用户验证过滤器");
        
        // 获取用户请求数据
        Object userRequest = context.get("userRequest");
        if (userRequest == null) {
            throw new IllegalArgumentException("用户请求数据不能为空");
        }
        
        // 这里可以添加具体的验证逻辑
        validateUserData(userRequest);
        
        // 继续执行下一个过滤器
        filterChain.filter(context);
    }
    
    private void validateUserData(Object userRequest) {
        // 具体的用户数据验证逻辑
        log.info("验证用户数据: {}", userRequest);
    }

    @Override
    public int getOrder() {
        return 1;
    }
} 
 