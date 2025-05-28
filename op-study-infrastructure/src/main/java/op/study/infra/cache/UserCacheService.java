package op.study.infra.cache;

import domain.entity.UserEntity;
import domain.entity.UserId;

import java.util.List;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户缓存服务接口
 */
public interface UserCacheService {

    /**
     * 缓存用户信息
     * @param userEntity 用户实体
     */
    void cacheUser(UserEntity userEntity);

    /**
     * 获取缓存的用户信息
     * @param userId 用户ID
     * @return 用户实体
     */
    UserEntity getCachedUser(UserId userId);

    /**
     * 根据用户名获取缓存的用户信息
     * @param userName 用户名
     * @return 用户实体
     */
    UserEntity getCachedUserByName(String userName);

    /**
     * 根据手机号获取缓存的用户信息
     * @param mobilePhone 手机号
     * @return 用户实体
     */
    UserEntity getCachedUserByMobile(String mobilePhone);

    /**
     * 删除用户缓存
     * @param userId 用户ID
     */
    void evictUser(UserId userId);

    /**
     * 删除用户相关的所有缓存
     * @param userEntity 用户实体
     */
    void evictUserAll(UserEntity userEntity);

    /**
     * 延迟删除缓存（用于双写一致性）
     * @param userId 用户ID
     */
    void delayedEvictUser(UserId userId);

    /**
     * 延迟删除用户相关的所有缓存
     * @param userEntity 用户实体
     */
    void delayedEvictUserAll(UserEntity userEntity);

    /**
     * 缓存用户列表
     * @param key 缓存键
     * @param users 用户列表
     */
    void cacheUserList(String key, List<UserEntity> users);

    /**
     * 获取缓存的用户列表
     * @param key 缓存键
     * @return 用户列表
     */
    List<UserEntity> getCachedUserList(String key);

    /**
     * 删除用户列表缓存
     * @param key 缓存键
     */
    void evictUserList(String key);

    /**
     * 预热用户缓存
     * @param userId 用户ID
     */
    void warmUpUserCache(UserId userId);

    /**
     * 批量预热用户缓存
     * @param userIds 用户ID列表
     */
    void batchWarmUpUserCache(List<UserId> userIds);
} 