package com.example.iface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import op.study.infra.cache.CacheService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xxs
 * @Date 2024/12/19
 * 缓存测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheTestController {

    private final CacheService cacheService;

    /**
     * 测试设置缓存
     */
    @PostMapping("/set")
    public Map<String, Object> setCache(@RequestParam String key, @RequestParam String value) {
        Map<String, Object> result = new HashMap<>();
        try {
            cacheService.set(key, value, 300, TimeUnit.SECONDS);
            result.put("success", true);
            result.put("message", "缓存设置成功");
            result.put("key", key);
            result.put("value", value);
            log.info("设置缓存: key={}, value={}", key, value);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "缓存设置失败: " + e.getMessage());
            log.error("设置缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
        return result;
    }

    /**
     * 测试获取缓存
     */
    @GetMapping("/get")
    public Map<String, Object> getCache(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            Object value = cacheService.get(key);
            result.put("success", true);
            result.put("key", key);
            result.put("value", value);
            result.put("found", value != null);
            log.info("获取缓存: key={}, value={}, found={}", key, value, value != null);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取缓存失败: " + e.getMessage());
            log.error("获取缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
        return result;
    }

    /**
     * 测试删除缓存
     */
    @DeleteMapping("/delete")
    public Map<String, Object> deleteCache(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean deleted = cacheService.delete(key);
            result.put("success", true);
            result.put("key", key);
            result.put("deleted", deleted);
            log.info("删除缓存: key={}, deleted={}", key, deleted);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除缓存失败: " + e.getMessage());
            log.error("删除缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
        return result;
    }

    /**
     * 测试缓存是否存在
     */
    @GetMapping("/exists")
    public Map<String, Object> existsCache(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean exists = cacheService.hasKey(key);
            result.put("success", true);
            result.put("key", key);
            result.put("exists", exists);
            log.info("检查缓存存在: key={}, exists={}", key, exists);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "检查缓存失败: " + e.getMessage());
            log.error("检查缓存失败: key={}, error={}", key, e.getMessage(), e);
        }
        return result;
    }
} 