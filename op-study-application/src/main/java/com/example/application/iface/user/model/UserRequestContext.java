package com.example.application.iface.user.model;

import com.example.application.iface.model.UserRequest;
import com.example.opstudycommon.filter.context.AbstractContext;
import com.example.opstudycommon.filter.selector.FilterSelector;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xxs
 * @Date 2024/7/2 22:35
 * 用户请求上下文
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserRequestContext extends AbstractContext {

    @Getter
    @Setter
    private FilterSelector filterSelector;

    @Getter
    @Setter
    private UserRequest userRequest;

    /**
     * 请求时间
     */
    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    /**
     * 请求ID，用于链路追踪
     */
    private String requestId;

    /**
     * 操作用户ID
     */
    private Long operatorId;

    /**
     * 扩展属性
     */
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    public UserRequestContext(FilterSelector filterSelector, UserRequest userRequest, 
                              LocalDateTime requestTime, String requestId, Long operatorId, 
                              Map<String, Object> attributes) {
        super(filterSelector);
        this.filterSelector = filterSelector;
        this.userRequest = userRequest;
        this.requestTime = requestTime != null ? requestTime : LocalDateTime.now();
        this.requestId = requestId;
        this.operatorId = operatorId;
        this.attributes = attributes != null ? attributes : new HashMap<>();
    }

    @Override
    public boolean continueChain() {
        return true;
    }

    /**
     * 添加属性
     */
    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * 获取属性
     */
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * 获取属性，带默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T defaultValue) {
        Object value = this.attributes.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 获取对象（兼容Context接口）
     */
    public Object get(String key) {
        if ("userRequest".equals(key)) {
            return this.userRequest;
        }
        return getAttribute(key);
    }

    /**
     * 检查是否包含属性
     */
    public boolean hasAttribute(String key) {
        return this.attributes.containsKey(key);
    }
}
