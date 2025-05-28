package op.study.infra.cache.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import op.study.infra.cache.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xxs
 * @Date 2024/12/19
 * Redis缓存服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Lua脚本：释放分布式锁
    private static final String RELEASE_LOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else " +
        "return 0 " +
        "end";

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("设置缓存成功: key={}", key);
        } catch (Exception e) {
            log.error("设置缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.debug("设置缓存成功: key={}, timeout={} {}", key, timeout, unit);
        } catch (Exception e) {
            log.error("设置缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
    }

    @Override
    public Object get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            log.debug("获取缓存: key={}, found={}", key, value != null);
            return value;
        } catch (Exception e) {
            log.error("获取缓存失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = get(key);
            if (value == null) {
                return null;
            }
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            log.warn("缓存值类型不匹配: key={}, expected={}, actual={}", 
                    key, clazz.getSimpleName(), value.getClass().getSimpleName());
            return null;
        } catch (Exception e) {
            log.error("获取缓存失败: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("删除缓存: key={}, success={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("删除缓存失败: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Long deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys == null || keys.isEmpty()) {
                return 0L;
            }
            Long result = redisTemplate.delete(keys);
            log.debug("批量删除缓存: pattern={}, count={}", pattern, result);
            return result;
        } catch (Exception e) {
            log.error("批量删除缓存失败: pattern={}, error={}", pattern, e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public Boolean hasKey(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            log.debug("检查缓存存在: key={}, exists={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("检查缓存存在失败: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            log.debug("设置过期时间: key={}, timeout={} {}, success={}", key, timeout, unit, result);
            return result;
        } catch (Exception e) {
            log.error("设置过期时间失败: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key);
            log.debug("获取过期时间: key={}, expire={}秒", key, expire);
            return expire;
        } catch (Exception e) {
            log.error("获取过期时间失败: key={}, error={}", key, e.getMessage(), e);
            return -1L;
        }
    }

    @Override
    public Boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
            log.debug("尝试获取分布式锁: lockKey={}, requestId={}, expireTime={}秒, success={}", 
                    lockKey, requestId, expireTime, result);
            return result;
        } catch (Exception e) {
            log.error("获取分布式锁失败: lockKey={}, error={}", lockKey, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean releaseLock(String lockKey, String requestId) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(RELEASE_LOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), requestId);
            boolean success = result != null && result == 1L;
            log.debug("释放分布式锁: lockKey={}, requestId={}, success={}", lockKey, requestId, success);
            return success;
        } catch (Exception e) {
            log.error("释放分布式锁失败: lockKey={}, error={}", lockKey, e.getMessage(), e);
            return false;
        }
    }
} 