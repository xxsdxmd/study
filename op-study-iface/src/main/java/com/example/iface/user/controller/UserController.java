package com.example.iface.user.controller;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.iface.user.UserIFace;
import com.example.opstudycommon.result.Result;
import domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关的API接口")
public class UserController {

    private final UserIFace userIFace;
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public Result<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("用户注册请求: userName={}, mobilePhone={}", 
                userRequest.getUserName(), userRequest.getMobilePhone());
        return userIFace.registerUser(userRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录验证")
    public Result<?> loginUser(
            @Parameter(description = "登录名（用户名/手机号/邮箱）") @RequestParam String loginName,
            @Parameter(description = "密码") @RequestParam String password) {
        log.info("用户登录请求: loginName={}", loginName);
        return userService.loginUser(loginName, password);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    public Result<?> updateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Valid @RequestBody UserRequest userRequest) {
        log.info("用户更新请求: userId={}, userName={}", userId, userRequest.getUserName());
        userRequest.setId(userId);
        return userService.updateUser(userRequest);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "查询用户信息", description = "根据用户ID查询用户详细信息")
    public Result<UserEntity> getUserById(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        log.info("查询用户请求: userId={}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "激活指定的用户")
    public Result<?> activateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        log.info("激活用户请求: userId={}", userId);
        return userService.activateUser(userId);
    }

    @PostMapping("/{userId}/deactivate")
    @Operation(summary = "停用用户", description = "停用指定的用户")
    public Result<?> deactivateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        log.info("停用用户请求: userId={}", userId);
        return userService.deactivateUser(userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "软删除指定的用户")
    public Result<?> deleteUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        log.info("删除用户请求: userId={}", userId);
        try {
            userService.deleteUser(userId);
            return Result.success("用户删除成功");
        } catch (Exception e) {
            log.error("删除用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("删除用户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/change-password")
    @Operation(summary = "修改密码", description = "用户修改密码")
    public Result<?> changePassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        log.info("修改密码请求: userId={}", userId);
        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("修改密码失败: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/reset-password")
    @Operation(summary = "重置密码", description = "管理员重置用户密码")
    public Result<?> resetPassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        log.info("重置密码请求: userId={}", userId);
        try {
            userService.resetPassword(userId, newPassword);
            return Result.success("密码重置成功");
        } catch (Exception e) {
            log.error("重置密码失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("重置密码失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "搜索用户", description = "根据关键字搜索用户")
    public Result<?> searchUsers(
            @Parameter(description = "搜索关键字") @RequestParam String keyword) {
        log.info("搜索用户请求: keyword={}", keyword);
        try {
            List<UserEntity> users = userService.searchUsers(keyword);
            return Result.success("搜索成功", users);
        } catch (Exception e) {
            log.error("搜索用户失败: keyword={}, error={}", keyword, e.getMessage(), e);
            return Result.error("搜索用户失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
    public Result<?> getUsersByPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询用户请求: pageNum={}, pageSize={}", pageNum, pageSize);
        try {
            List<UserEntity> users = userService.findUsersByPage(pageNum, pageSize);
            return Result.success("查询成功", users);
        } catch (Exception e) {
            log.error("分页查询用户失败: pageNum={}, pageSize={}, error={}", 
                    pageNum, pageSize, e.getMessage(), e);
            return Result.error("分页查询用户失败: " + e.getMessage());
        }
    }
} 