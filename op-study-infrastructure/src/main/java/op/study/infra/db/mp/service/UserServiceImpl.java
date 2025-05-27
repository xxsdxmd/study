package op.study.infra.db.mp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.opstudycommon.enums.EnumUserType;
import com.example.opstudycommon.enums.UserStatus;
import com.example.opstudycommon.support.EntityOperations;
import lombok.extern.slf4j.Slf4j;
import op.study.infra.db.mp.dao.UserDao;
import op.study.infra.db.mp.event.UserCreateEvent;
import op.study.infra.db.mp.event.UserUpdateEvent;
import op.study.infra.db.mp.mapper.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/30 15:27
 * 用户数据服务实现
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDao> implements UserService {

    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Long create(UserDao userDao) {
        if (userDao == null) {
            throw new IllegalArgumentException("用户数据不能为空");
        }
        
        // 直接使用MyBatis Plus的方法
        boolean result = save(userDao);
        if (result) {
            log.info("用户创建成功: {}", userDao.getUserName());
            eventPublisher.publishEvent(new UserCreateEvent(userDao, EnumUserType.CREATE));
            return userDao.getUserId();
        }
        return null;
    }

    @Override
    @Transactional
    public void updateUser(UserDao userDao) {
        if (userDao == null || userDao.getUserId() == null) {
            throw new IllegalArgumentException("用户数据或用户ID不能为空");
        }
        
        // 直接使用MyBatis Plus的方法
        boolean result = updateById(userDao);
        if (result) {
            log.info("用户更新成功: {}", userDao.getUserName());
            eventPublisher.publishEvent(new UserUpdateEvent(userDao, EnumUserType.UPDATE));
        }
    }

    @Override
    public UserDao findByUserName(String userName) {
        if (!StringUtils.hasText(userName)) {
            return null;
        }
        return getBaseMapper().findByUserName(userName);
    }

    @Override
    public UserDao findByMobilePhone(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return null;
        }
        return getBaseMapper().findByMobilePhone(mobilePhone);
    }

    @Override
    public UserDao findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return getBaseMapper().findByEmail(email);
    }

    @Override
    public UserDao findByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return getBaseMapper().findByUserId(userId);
    }

    @Override
    public List<UserDao> findByStatus(Integer status) {
        if (status == null) {
            return List.of();
        }
        return getBaseMapper().findByStatus(status);
    }

    @Override
    public IPage<UserDao> findUsersByPage(int pageNum, int pageSize) {
        Page<UserDao> page = new Page<>(pageNum, pageSize);
        return getBaseMapper().selectUserPage(page);
    }

    @Override
    @Transactional
    public boolean updateLastLoginTime(Long userId, LocalDateTime lastLoginTime) {
        if (userId == null || lastLoginTime == null) {
            return false;
        }
        
        int result = getBaseMapper().updateLastLoginTime(userId, lastLoginTime, LocalDateTime.now());
        if (result > 0) {
            log.info("更新用户最后登录时间成功: userId={}", userId);
        }
        return result > 0;
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (!StringUtils.hasText(userName)) {
            return false;
        }
        return getBaseMapper().countByUserName(userName) > 0;
    }

    @Override
    public boolean existsByMobilePhone(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return false;
        }
        return getBaseMapper().countByMobilePhone(mobilePhone) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return getBaseMapper().countByEmail(email) > 0;
    }

    @Override
    public List<UserDao> findByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        return getBaseMapper().findByUserIds(userIds);
    }

    @Override
    public List<UserDao> searchUsers(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        return getBaseMapper().searchUsers(keyword);
    }

    @Override
    @Transactional
    public boolean softDeleteUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        LambdaUpdateWrapper<UserDao> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserDao::getUserId, userId)
                .set(UserDao::getStatus, UserStatus.DELETED.getCode())
                .set(UserDao::getUpdateTime, LocalDateTime.now());
        
        boolean result = update(updateWrapper);
        if (result) {
            log.info("软删除用户成功: userId={}", userId);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean activateUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        LambdaUpdateWrapper<UserDao> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserDao::getUserId, userId)
                .set(UserDao::getStatus, UserStatus.ACTIVE.getCode())
                .set(UserDao::getUpdateTime, LocalDateTime.now());
        
        boolean result = update(updateWrapper);
        if (result) {
            log.info("激活用户成功: userId={}", userId);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deactivateUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        LambdaUpdateWrapper<UserDao> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserDao::getUserId, userId)
                .set(UserDao::getStatus, UserStatus.DISABLED.getCode())
                .set(UserDao::getUpdateTime, LocalDateTime.now());
        
        boolean result = update(updateWrapper);
        if (result) {
            log.info("停用用户成功: userId={}", userId);
        }
        return result;
    }
}
