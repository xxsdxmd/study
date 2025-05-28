package com.example.iface;

import com.example.application.iface.model.UserRequest;
import com.example.application.iface.user.UserService;
import com.example.opstudycommon.result.Result;
import common.util.SnowflakeIdGenerator;
import domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxs
 * @Date 2024/12/19
 * 分库分表测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sharding")
@RequiredArgsConstructor
public class ShardingTestController {

    private final UserService userService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    /**
     * 测试雪花算法ID生成
     */
    @GetMapping("/snowflake/generate")
    public Map<String, Object> generateSnowflakeId() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ids.add(snowflakeIdGenerator.nextId());
            }
            
            result.put("success", true);
            result.put("message", "雪花算法ID生成成功");
            result.put("ids", ids);
            result.put("count", ids.size());
            
            log.info("生成雪花算法ID: {}", ids);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "ID生成失败: " + e.getMessage());
            log.error("雪花算法ID生成失败", e);
        }
        return result;
    }

    /**
     * 批量创建用户测试分库分表
     */
    @PostMapping("/users/batch")
    public Map<String, Object> batchCreateUsers(@RequestParam(defaultValue = "10") int count) {
        Map<String, Object> result = new HashMap<>();
        List<Long> createdUserIds = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try {
            log.info("开始批量创建用户测试分库分表: count={}", count);
            
            for (int i = 0; i < count; i++) {
                try {
                    UserRequest userRequest = new UserRequest();
                    userRequest.setUserName("sharding_user_" + i + "_" + System.currentTimeMillis());
                    userRequest.setMobilePhone("138" + String.format("%08d", i));
                    userRequest.setEmail("sharding_user_" + i + "@example.com");
                    
                    Result<?> createResult = userService.registerUser(userRequest);
                    if (createResult.isSuccess()) {
                        createdUserIds.add((Long) createResult.getData());
                        log.debug("创建用户成功: userId={}, userName={}", 
                                createResult.getData(), userRequest.getUserName());
                    } else {
                        errors.add("用户" + i + "创建失败: " + createResult.getMsg());
                    }
                } catch (Exception e) {
                    errors.add("用户" + i + "创建异常: " + e.getMessage());
                    log.error("创建用户{}失败", i, e);
                }
            }
            
            result.put("success", true);
            result.put("message", "批量创建用户完成");
            result.put("totalCount", count);
            result.put("successCount", createdUserIds.size());
            result.put("failCount", errors.size());
            result.put("createdUserIds", createdUserIds);
            result.put("errors", errors);
            
            log.info("批量创建用户完成: 总数={}, 成功={}, 失败={}", 
                    count, createdUserIds.size(), errors.size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量创建用户失败: " + e.getMessage());
            log.error("批量创建用户失败", e);
        }
        
        return result;
    }

    /**
     * 测试分库分表查询
     */
    @GetMapping("/users/{userId}")
    public Map<String, Object> getUserBySharding(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("测试分库分表查询用户: userId={}", userId);
            
            Result<UserEntity> queryResult = userService.getUserById(userId);
            
            if (queryResult.isSuccess()) {
                UserEntity user = queryResult.getData();
                result.put("success", true);
                result.put("message", "查询成功");
                result.put("user", user);
                result.put("shardingInfo", getShardingInfo(userId));
                
                log.info("分库分表查询成功: userId={}, userName={}", 
                        userId, user.getUserName());
            } else {
                result.put("success", false);
                result.put("message", queryResult.getMsg());
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
            log.error("分库分表查询失败: userId={}", userId, e);
        }
        
        return result;
    }

    /**
     * 获取分库分表路由信息
     */
    @GetMapping("/route/info/{userId}")
    public Map<String, Object> getShardingRouteInfo(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> shardingInfo = getShardingInfo(userId);
            
            result.put("success", true);
            result.put("message", "路由信息获取成功");
            result.put("userId", userId);
            result.put("shardingInfo", shardingInfo);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取路由信息失败: " + e.getMessage());
            log.error("获取分库分表路由信息失败: userId={}", userId, e);
        }
        
        return result;
    }

    /**
     * 计算分库分表路由信息
     */
    private Map<String, Object> getShardingInfo(Long userId) {
        Map<String, Object> shardingInfo = new HashMap<>();
        
        // 分库逻辑：用户ID % 16
        int dbIndex = Math.abs(userId.hashCode()) % 16;
        String dataSourceName = "ds" + dbIndex;
        String databaseName = "user_db_" + String.format("%02d", dbIndex);
        
        // 分表逻辑：用户ID % 2048
        long tableIndex = Math.abs(userId.hashCode()) % (16 * 128);
        String tableName = "t_user_" + String.format("%04d", tableIndex);
        
        shardingInfo.put("userId", userId);
        shardingInfo.put("userIdHash", userId.hashCode());
        shardingInfo.put("dbIndex", dbIndex);
        shardingInfo.put("dataSourceName", dataSourceName);
        shardingInfo.put("databaseName", databaseName);
        shardingInfo.put("tableIndex", tableIndex);
        shardingInfo.put("tableName", tableName);
        shardingInfo.put("fullTableName", databaseName + "." + tableName);
        
        return shardingInfo;
    }
} 