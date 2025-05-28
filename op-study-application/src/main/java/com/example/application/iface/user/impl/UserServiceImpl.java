package com.example.application.iface.user.impl;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.application.iface.user.factory.FilterSelectorFactory;
import com.example.application.iface.user.model.UserRequestContext;
import com.example.opstudycommon.enums.EnumFilterSelectorScene;
import com.example.opstudycommon.enums.UserStatus;
import com.example.opstudycommon.filter.selector.FilterSelector;
import com.example.opstudycommon.result.Result;
import com.google.common.collect.Lists;
import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.service.user.iface.UserDomainService;
import domain.service.user.UserIdGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author xxs
 * @Date 2024/6/30 21:02
 * 应用层服务实现 - 主要做流程编排
 */
@Slf4j
@Service("userApplicationService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FilterSelectorFactory filterSelectorFactory;
    private final UserDomainService userDomainService;
    private final UserIdGeneratorService userIdGeneratorService;

    @Override
    @Transactional
    public Result<?> registerUser(@Valid UserRequest userRequest) {
        try {
            log.info("开始用户注册流程: userName={}, mobilePhone={}", 
                    userRequest.getUserName(), userRequest.getMobilePhone());

            // 1. 构建过滤器选择器
            FilterSelector filterSelector = filterSelectorFactory.getFilterSelector(EnumFilterSelectorScene.REGISTER);
            
            // 2. 构建用户请求上下文
            UserRequestContext context = buildUserRequestContext(userRequest, filterSelector);
            
            // 3. 执行过滤器链
            executeFilterChain(context);
            
            // 4. 构建用户实体
            UserEntity userEntity = buildUserEntity(userRequest);
            
            // 5. 调用领域服务创建用户
            UserEntity createdUser = userDomainService.createUser(userEntity);
            
            log.info("用户注册成功: userId={}, userName={}", 
                    createdUser.getUserId().getUserId(), createdUser.getUserName());
            
            return Result.success("用户注册成功", createdUser.getUserId().getUserId());
            
        } catch (Exception e) {
            log.error("用户注册失败: userName={}, error={}", userRequest.getUserName(), e.getMessage(), e);
            return Result.error("用户注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    public Result<?> loginUser(String loginName, String password) {
        try {
            log.info("开始用户登录流程: loginName={}", loginName);

            // 参数验证
            if (!StringUtils.hasText(loginName) || !StringUtils.hasText(password)) {
                return Result.paramError("登录名和密码不能为空");
            }

            // 用户认证
            Optional<UserEntity> userOpt = userDomainService.authenticate(loginName, password);
            if (!userOpt.isPresent()) {
                return Result.error("用户名或密码错误");
            }

            UserEntity user = userOpt.get();
            
            // 更新最后登录时间
            userDomainService.updateLastLoginTime(user.getUserId());
            
            log.info("用户登录成功: userId={}, userName={}", 
                    user.getUserId().getUserId(), user.getUserName());
            
            return Result.success("登录成功", user.getUserId().getUserId());
            
        } catch (Exception e) {
            log.error("用户登录失败: loginName={}, error={}", loginName, e.getMessage(), e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    public Result<?> updateUser(@Valid UserRequest userRequest) {
        try {
            log.info("开始用户更新流程: userId={}, userName={}", 
                    userRequest.getId(), userRequest.getUserName());

            // 1. 构建过滤器选择器
            FilterSelector filterSelector = filterSelectorFactory.getFilterSelector(EnumFilterSelectorScene.UPDATE);
            
            // 2. 构建用户请求上下文
            UserRequestContext context = buildUserRequestContext(userRequest, filterSelector);
            
            // 3. 执行过滤器链
            executeFilterChain(context);
            
            // 4. 查询现有用户
            UserEntity existingUser = userDomainService.selectById(new UserId(userRequest.getId()));
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
            
            // 5. 更新用户信息
            existingUser.updateUserInfo(userRequest.getUserName(), null);
            if (StringUtils.hasText(userRequest.getMobilePhone())) {
                existingUser.setMobilePhone(userRequest.getMobilePhone());
            }
            
            // 6. 保存更新
            UserEntity updatedUser = userDomainService.updateUser(existingUser);
            
            log.info("用户更新成功: userId={}, userName={}", 
                    updatedUser.getUserId().getUserId(), updatedUser.getUserName());
            
            return Result.success("用户更新成功");
            
        } catch (Exception e) {
            log.error("用户更新失败: userId={}, error={}", userRequest.getId(), e.getMessage(), e);
            return Result.error("用户更新失败: " + e.getMessage());
        }
    }

    /**
     * 查询用户信息
     */
    public Result<UserEntity> getUserById(Long userId) {
        try {
            if (userId == null) {
                return Result.paramError("用户ID不能为空");
            }

            UserEntity user = userDomainService.selectById(new UserId(userId));
            if (user == null) {
                return Result.error("用户不存在");
            }

            return Result.success("查询成功", user);
            
        } catch (Exception e) {
            log.error("查询用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 激活用户
     */
    public Result<?> activateUser(Long userId) {
        try {
            if (userId == null) {
                return Result.paramError("用户ID不能为空");
            }

            userDomainService.activateUser(new UserId(userId));
            log.info("用户激活成功: userId={}", userId);
            
            return Result.success("用户激活成功");
            
        } catch (Exception e) {
            log.error("用户激活失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("用户激活失败: " + e.getMessage());
        }
    }

    /**
     * 停用用户
     */
    public Result<?> deactivateUser(Long userId) {
        try {
            if (userId == null) {
                return Result.paramError("用户ID不能为空");
            }

            userDomainService.deactivateUser(new UserId(userId));
            log.info("用户停用成功: userId={}", userId);
            
            return Result.success("用户停用成功");
            
        } catch (Exception e) {
            log.error("用户停用失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("用户停用失败: " + e.getMessage());
        }
    }

    /**
     * 构建用户请求上下文
     */
    private UserRequestContext buildUserRequestContext(UserRequest userRequest, FilterSelector filterSelector) {
        return UserRequestContext.builder()
                .filterSelector(filterSelector)
                .userRequest(userRequest)
                .requestId(UUID.randomUUID().toString())
                .build();
    }

    /**
     * 执行过滤器链
     */
    private void executeFilterChain(UserRequestContext context) {
        try {
            // 获取过滤器选择器中的过滤器名称列表
            FilterSelector filterSelector = context.getFilterSelector();
            if (filterSelector != null && !filterSelector.getFilterNames().isEmpty()) {
                log.debug("开始执行过滤器链，过滤器数量: {}", filterSelector.getFilterNames().size());
                
                // 这里可以根据过滤器名称动态加载和执行过滤器
                // 暂时简化处理，只记录日志
                for (String filterName : filterSelector.getFilterNames()) {
                    log.debug("执行过滤器: {}", filterName);
                    // 实际项目中这里会根据filterName从Spring容器中获取对应的Filter实例并执行
                }
            }
            
            log.debug("过滤器链执行完成");
        } catch (Exception e) {
            log.error("过滤器链执行失败: {}", e.getMessage(), e);
            throw new RuntimeException("过滤器链执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除用户
     */
    @Override
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        userDomainService.deleteUser(new UserId(userId));
    }

    /**
     * 修改密码
     */
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        userDomainService.changePassword(new UserId(userId), oldPassword, newPassword);
    }

    /**
     * 重置密码
     */
    @Override
    public void resetPassword(Long userId, String newPassword) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        userDomainService.resetPassword(new UserId(userId), newPassword);
    }

    /**
     * 搜索用户
     */
    @Override
    public List<UserEntity> searchUsers(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Lists.newArrayList();
        }
        // 这里可以调用领域服务进行搜索
        // 暂时返回空列表
        return Lists.newArrayList();
    }

    /**
     * 分页查询用户
     */
    @Override
    public List<UserEntity> findUsersByPage(int pageNum, int pageSize) {
        return userDomainService.findUsersByPage(pageNum, pageSize);
    }

    /**
     * 构建用户实体
     */
    private UserEntity buildUserEntity(UserRequest userRequest) {
        // 使用雪花算法生成用户ID
        UserId userId = userIdGeneratorService.generateUserId();
        
        return UserEntity.builder()
                .userId(userId)  // 设置生成的用户ID
                .userName(userRequest.getUserName())
                .mobilePhone(userRequest.getMobilePhone())
                .email(userRequest.getEmail())
                .status(UserStatus.INACTIVE) // 默认未激活状态
                .build();
    }
}
