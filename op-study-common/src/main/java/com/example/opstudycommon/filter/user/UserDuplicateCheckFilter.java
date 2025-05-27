package com.example.opstudycommon.filter.user;

import com.example.opstudycommon.filter.AbstractFilter;
import com.example.opstudycommon.filter.FilterChain;
import com.example.opstudycommon.filter.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户重复检查过滤器
 */
@Slf4j
@Component("userDuplicateCheckFilter")
public class UserDuplicateCheckFilter extends AbstractFilter<Context> {

    @Override
    protected void handle(Context context) {
        log.info("执行用户重复检查过滤器");
        
        // 检查用户是否已存在
        checkUserDuplicate(context);
    }
    
    private void checkUserDuplicate(Context context) {
        // 这里可以添加具体的重复检查逻辑
        // 比如检查用户名、手机号是否已存在
        log.info("检查用户重复性");
        
        // 模拟检查逻辑
        Object userRequest = context.get("userRequest");
        if (userRequest != null) {
            // 实际项目中这里会调用repository检查数据库
            log.info("用户重复性检查通过");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}