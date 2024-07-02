package com.example.application.iface.user.filter;

import com.example.application.iface.user.model.UserRequestContext;
import com.example.opstudycommon.filter.AbstractFilter;
import org.apache.commons.lang3.StringUtils;


/**
 * @author xxs
 * @Date 2024/7/2 22:45
 */
public class UserMobilePhoneFilter extends AbstractFilter<UserRequestContext> {


    @Override
    protected void handle(UserRequestContext userRequestContext) {
        if (!judgeMobilePhone(userRequestContext.getUserRequest().getMobilePhone())) {
            throw new RuntimeException("mobile phone is not valid");
        }
    }

    /**
     * judge逻辑
     * @param mobilePhone
     * @return
     */
    private boolean judgeMobilePhone(String mobilePhone) {
        return StringUtils.isNoneBlank(mobilePhone);
    }
}
