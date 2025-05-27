package domain.service.user.iface.impl;

import com.example.opstudycommon.enums.UserStatus;
import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.UserRepository;
import domain.service.user.iface.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author xxs
 * @Date 2024/7/1 22:31
 * 用户领域服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDomainService {

    private final UserRepository userRepository;

    @Override
    public UserEntity selectById(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return userRepository.find(userId);
    }

    @Override
    public Optional<UserEntity> findById(UserId userId) {
        try {
            UserEntity user = selectById(userId);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.warn("查询用户失败: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByUserName(String userName) {
        if (!StringUtils.hasText(userName)) {
            return Optional.empty();
        }
        try {
            return userRepository.findByUserName(userName);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败: userName={}, error={}", userName, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByMobilePhone(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return Optional.empty();
        }
        try {
            return userRepository.findByMobilePhone(mobilePhone);
        } catch (Exception e) {
            log.error("根据手机号查询用户失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("根据邮箱查询用户失败: email={}, error={}", email, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("用户实体不能为空");
        }
        
        // 验证用户数据
        userEntity.validate();
        
        // 检查重复性
        if (existsByUserName(userEntity.getUserName())) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (existsByMobilePhone(userEntity.getMobilePhone())) {
            throw new IllegalArgumentException("手机号已存在");
        }
        if (StringUtils.hasText(userEntity.getEmail()) && existsByEmail(userEntity.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        
        // 设置默认状态
        if (userEntity.getStatus() == null) {
            userEntity.setStatus(UserStatus.INACTIVE);
        }
        
        // 保存用户
        userRepository.save(userEntity);
        log.info("创建用户成功: {}", userEntity.getUserName());
        
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        if (userEntity == null || userEntity.getId() == null) {
            throw new IllegalArgumentException("用户实体或ID不能为空");
        }
        
        // 验证用户数据
        userEntity.validate();
        
        // 检查用户是否存在
        UserEntity existingUser = selectById(userEntity.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        // 保存更新
        userRepository.save(userEntity);
        log.info("更新用户成功: {}", userEntity.getUserName());
        
        return userEntity;
    }

    @Override
    public void deleteUser(UserId userId) {
        UserEntity user = selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        user.delete();
        userRepository.save(user);
        log.info("删除用户成功: {}", userId.getUserId());
    }

    @Override
    public void activateUser(UserId userId) {
        UserEntity user = selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        user.activate();
        userRepository.save(user);
        log.info("激活用户成功: {}", userId.getUserId());
    }

    @Override
    public void deactivateUser(UserId userId) {
        UserEntity user = selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        user.deactivate();
        userRepository.save(user);
        log.info("停用用户成功: {}", userId.getUserId());
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (!StringUtils.hasText(userName)) {
            return false;
        }
        try {
            return userRepository.existsByUserName(userName);
        } catch (Exception e) {
            log.error("检查用户名是否存在失败: userName={}, error={}", userName, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByMobilePhone(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return false;
        }
        try {
            return userRepository.existsByMobilePhone(mobilePhone);
        } catch (Exception e) {
            log.error("检查手机号是否存在失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            log.error("检查邮箱是否存在失败: email={}, error={}", email, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Optional<UserEntity> authenticate(String loginName, String password) {
        if (!StringUtils.hasText(loginName) || !StringUtils.hasText(password)) {
            return Optional.empty();
        }
        
        // 尝试通过不同方式查找用户
        Optional<UserEntity> userOpt = findByUserName(loginName);
        if (!userOpt.isPresent()) {
            userOpt = findByMobilePhone(loginName);
        }
        if (!userOpt.isPresent()) {
            userOpt = findByEmail(loginName);
        }
        
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            // 这里应该使用加密后的密码进行比较
            if (password.equals(user.getPassword()) && user.isAvailable()) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }

    @Override
    public void updateLastLoginTime(UserId userId) {
        UserEntity user = selectById(userId);
        if (user != null) {
            user.updateLastLoginTime();
            userRepository.save(user);
            log.info("更新用户最后登录时间: {}", userId.getUserId());
        }
    }

    @Override
    public void changePassword(UserId userId, String oldPassword, String newPassword) {
        UserEntity user = selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        // 验证旧密码
        if (!oldPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("旧密码不正确");
        }
        
        user.changePassword(newPassword);
        userRepository.save(user);
        log.info("修改用户密码成功: {}", userId.getUserId());
    }

    @Override
    public void resetPassword(UserId userId, String newPassword) {
        UserEntity user = selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        user.changePassword(newPassword);
        userRepository.save(user);
        log.info("重置用户密码成功: {}", userId.getUserId());
    }

    @Override
    public List<UserEntity> findUsersByPage(int pageNum, int pageSize) {
        try {
            return userRepository.findUsersByPage(pageNum, pageSize);
        } catch (Exception e) {
            log.error("分页查询用户失败: pageNum={}, pageSize={}, error={}", pageNum, pageSize, e.getMessage(), e);
            return com.google.common.collect.Lists.newArrayList();
        }
    }

    @Override
    public List<UserEntity> findUsersByStatus(Integer status) {
        try {
            return userRepository.findUsersByStatus(status);
        } catch (Exception e) {
            log.error("根据状态查询用户失败: status={}, error={}", status, e.getMessage(), e);
            return com.google.common.collect.Lists.newArrayList();
        }
    }
}
