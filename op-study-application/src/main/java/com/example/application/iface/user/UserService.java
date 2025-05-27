package com.example.application.iface.user;

import com.example.application.iface.model.UserRequest;
import com.example.opstudycommon.result.Result;
import domain.entity.UserEntity;
import domain.entity.UserId;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/30 21:00
 * 应用层用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    Result<?> registerUser(@Valid UserRequest userRequest);

    /**
     * 用户登录
     */
    Result<?> loginUser(String loginName, String password);

    /**
     * 更新用户信息
     */
    Result<?> updateUser(@Valid UserRequest userRequest);

    /**
     * 查询用户信息
     */
    Result<UserEntity> getUserById(Long userId);

    /**
     * 激活用户
     */
    Result<?> activateUser(Long userId);

    /**
     * 停用用户
     */
    Result<?> deactivateUser(Long userId);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 搜索用户
     */
    List<UserEntity> searchUsers(String keyword);

    /**
     * 分页查询用户
     */
    List<UserEntity> findUsersByPage(int pageNum, int pageSize);
}
