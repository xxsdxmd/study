package op.study.infra.cache.impl;

import domain.entity.UserEntity;
import domain.entity.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import op.study.infra.cache.CacheService;
import op.study.infra.cache.UserCacheService;
import op.study.infra.config.RedisConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户缓存服务实现 - 实现双写一致性
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheServiceImpl implements UserCacheService {

    private final CacheService cacheService;
    private final RedisConfig.CacheProperties cacheProperties;

    // 缓存键前缀
    private static final String USER_CACHE_PREFIX = "user:";
    private static final String USER_NAME_CACHE_PREFIX = "user:name:";
    private static final String USER_MOBILE_CACHE_PREFIX = "user:mobile:";
    private static final String USER_LIST_CACHE_PREFIX = "user:list:";
    private static final String USER_LOCK_PREFIX = "lock:user:";

    @Override
    public void cacheUser(UserEntity userEntity) {
        if (userEntity == null || userEntity.getUserId() == null) {
            return;
        }

        try {
            String userId = userEntity.getUserId().getUserId().toString();
            String userKey = USER_CACHE_PREFIX + userId;
            
            // 缓存用户信息
            cacheService.set(userKey, userEntity, cacheProperties.getUserTtl(), TimeUnit.SECONDS);
            
            // 缓存用户名映射
            if (userEntity.getUserName() != null) {
                String userNameKey = USER_NAME_CACHE_PREFIX + userEntity.getUserName();
                cacheService.set(userNameKey, userEntity, cacheProperties.getUserTtl(), TimeUnit.SECONDS);
            }
            
            // 缓存手机号映射
            if (userEntity.getMobilePhone() != null) {
                String mobileKey = USER_MOBILE_CACHE_PREFIX + userEntity.getMobilePhone();
                cacheService.set(mobileKey, userEntity, cacheProperties.getUserTtl(), TimeUnit.SECONDS);
            }
            
            log.debug("缓存用户信息成功: userId={}, userName={}", userId, userEntity.getUserName());
        } catch (Exception e) {
            log.error("缓存用户信息失败: userId={}, error={}", 
                    userEntity.getUserId().getUserId(), e.getMessage(), e);
        }
    }

    @Override
    public UserEntity getCachedUser(UserId userId) {
        if (userId == null) {
            return null;
        }

        try {
            String userKey = USER_CACHE_PREFIX + userId.getUserId();
            UserEntity userEntity = cacheService.get(userKey, UserEntity.class);
            log.debug("获取缓存用户: userId={}, found={}", userId.getUserId(), userEntity != null);
            return userEntity;
        } catch (Exception e) {
            log.error("获取缓存用户失败: userId={}, error={}", userId.getUserId(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UserEntity getCachedUserByName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return null;
        }

        try {
            String userNameKey = USER_NAME_CACHE_PREFIX + userName;
            UserEntity userEntity = cacheService.get(userNameKey, UserEntity.class);
            log.debug("根据用户名获取缓存用户: userName={}, found={}", userName, userEntity != null);
            return userEntity;
        } catch (Exception e) {
            log.error("根据用户名获取缓存用户失败: userName={}, error={}", userName, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UserEntity getCachedUserByMobile(String mobilePhone) {
        if (mobilePhone == null || mobilePhone.trim().isEmpty()) {
            return null;
        }

        try {
            String mobileKey = USER_MOBILE_CACHE_PREFIX + mobilePhone;
            UserEntity userEntity = cacheService.get(mobileKey, UserEntity.class);
            log.debug("根据手机号获取缓存用户: mobilePhone={}, found={}", mobilePhone, userEntity != null);
            return userEntity;
        } catch (Exception e) {
            log.error("根据手机号获取缓存用户失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void evictUser(UserId userId) {
        if (userId == null) {
            return;
        }

        try {
            String userKey = USER_CACHE_PREFIX + userId.getUserId();
            cacheService.delete(userKey);
            log.debug("删除用户缓存: userId={}", userId.getUserId());
        } catch (Exception e) {
            log.error("删除用户缓存失败: userId={}, error={}", userId.getUserId(), e.getMessage(), e);
        }
    }

    @Override
    public void evictUserAll(UserEntity userEntity) {
        if (userEntity == null || userEntity.getUserId() == null) {
            return;
        }

        try {
            String userId = userEntity.getUserId().getUserId().toString();
            
            // 删除用户信息缓存
            String userKey = USER_CACHE_PREFIX + userId;
            cacheService.delete(userKey);
            
            // 删除用户名映射缓存
            if (userEntity.getUserName() != null) {
                String userNameKey = USER_NAME_CACHE_PREFIX + userEntity.getUserName();
                cacheService.delete(userNameKey);
            }
            
            // 删除手机号映射缓存
            if (userEntity.getMobilePhone() != null) {
                String mobileKey = USER_MOBILE_CACHE_PREFIX + userEntity.getMobilePhone();
                cacheService.delete(mobileKey);
            }
            
            log.debug("删除用户所有缓存: userId={}, userName={}", userId, userEntity.getUserName());
        } catch (Exception e) {
            log.error("删除用户所有缓存失败: userId={}, error={}", 
                    userEntity.getUserId().getUserId(), e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void delayedEvictUser(UserId userId) {
        if (userId == null) {
            return;
        }

        try {
            // 延迟删除，用于双写一致性
            Thread.sleep(cacheProperties.getDelayDeleteInterval());
            evictUser(userId);
            log.debug("延迟删除用户缓存: userId={}", userId.getUserId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("延迟删除用户缓存被中断: userId={}", userId.getUserId());
        } catch (Exception e) {
            log.error("延迟删除用户缓存失败: userId={}, error={}", userId.getUserId(), e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void delayedEvictUserAll(UserEntity userEntity) {
        if (userEntity == null || userEntity.getUserId() == null) {
            return;
        }

        try {
            // 延迟删除，用于双写一致性
            Thread.sleep(cacheProperties.getDelayDeleteInterval());
            evictUserAll(userEntity);
            log.debug("延迟删除用户所有缓存: userId={}", userEntity.getUserId().getUserId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("延迟删除用户所有缓存被中断: userId={}", userEntity.getUserId().getUserId());
        } catch (Exception e) {
            log.error("延迟删除用户所有缓存失败: userId={}, error={}", 
                    userEntity.getUserId().getUserId(), e.getMessage(), e);
        }
    }

    @Override
    public void cacheUserList(String key, List<UserEntity> users) {
        if (key == null || key.trim().isEmpty() || users == null) {
            return;
        }

        try {
            String listKey = USER_LIST_CACHE_PREFIX + key;
            cacheService.set(listKey, users, cacheProperties.getDefaultTtl(), TimeUnit.SECONDS);
            log.debug("缓存用户列表: key={}, size={}", key, users.size());
        } catch (Exception e) {
            log.error("缓存用户列表失败: key={}, error={}", key, e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserEntity> getCachedUserList(String key) {
        if (key == null || key.trim().isEmpty()) {
            return null;
        }

        try {
            String listKey = USER_LIST_CACHE_PREFIX + key;
            List<UserEntity> users = cacheService.get(listKey, List.class);
            log.debug("获取缓存用户列表: key={}, found={}", key, users != null);
            return users;
        } catch (Exception e) {
            log.error("获取缓存用户列表失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void evictUserList(String key) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }

        try {
            String listKey = USER_LIST_CACHE_PREFIX + key;
            cacheService.delete(listKey);
            log.debug("删除用户列表缓存: key={}", key);
        } catch (Exception e) {
            log.error("删除用户列表缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
    }

    @Override
    public void warmUpUserCache(UserId userId) {
        if (userId == null) {
            return;
        }

        String lockKey = USER_LOCK_PREFIX + "warmup:" + userId.getUserId();
        String requestId = UUID.randomUUID().toString();

        try {
            // 使用分布式锁防止重复预热
            if (cacheService.tryLock(lockKey, requestId, cacheProperties.getLockTimeout())) {
                try {
                    // 检查缓存是否已存在
                    UserEntity cachedUser = getCachedUser(userId);
                    if (cachedUser == null) {
                        // 这里应该从数据库加载用户信息并缓存
                        // 由于这是基础设施层，我们不直接调用领域服务
                        // 实际实现中可以通过事件或其他方式触发
                        log.debug("预热用户缓存: userId={}", userId.getUserId());
                    }
                } finally {
                    cacheService.releaseLock(lockKey, requestId);
                }
            }
        } catch (Exception e) {
            log.error("预热用户缓存失败: userId={}, error={}", userId.getUserId(), e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void batchWarmUpUserCache(List<UserId> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }

        log.info("开始批量预热用户缓存: count={}", userIds.size());
        for (UserId userId : userIds) {
            warmUpUserCache(userId);
        }
        log.info("批量预热用户缓存完成: count={}", userIds.size());
    }
} 