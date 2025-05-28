package op.study.infra.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author xxs
 * @Date 2024/12/19
 * 缓存服务接口
 */
public interface CacheService {

    /**
     * 设置缓存
     * @param key 缓存键
     * @param value 缓存值
     */
    void set(String key, Object value);

    /**
     * 设置缓存并指定过期时间
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取缓存
     * @param key 缓存键
     * @return 缓存值
     */
    Object get(String key);

    /**
     * 获取缓存并指定类型
     * @param key 缓存键
     * @param clazz 目标类型
     * @return 缓存值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     * @param key 缓存键
     * @return 是否删除成功
     */
    Boolean delete(String key);

    /**
     * 批量删除缓存
     * @param pattern 键模式
     * @return 删除的键数量
     */
    Long deleteByPattern(String pattern);

    /**
     * 判断缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    Boolean hasKey(String key);

    /**
     * 设置过期时间
     * @param key 缓存键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 获取过期时间
     * @param key 缓存键
     * @return 过期时间（秒）
     */
    Long getExpire(String key);

    /**
     * 分布式锁 - 尝试获取锁
     * @param lockKey 锁键
     * @param requestId 请求ID
     * @param expireTime 过期时间（秒）
     * @return 是否获取成功
     */
    Boolean tryLock(String lockKey, String requestId, long expireTime);

    /**
     * 分布式锁 - 释放锁
     * @param lockKey 锁键
     * @param requestId 请求ID
     * @return 是否释放成功
     */
    Boolean releaseLock(String lockKey, String requestId);
} 