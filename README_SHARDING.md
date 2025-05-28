# 分库分表实现方案

## 概述

本项目实现了基于ShardingSphere的分库分表解决方案，支持：
- **16个数据库**，每个数据库**128张表**，总共**2048张表**
- **雪花算法**生成全局唯一主键ID
- **按用户ID**进行分库分表路由
- **Redis缓存**与分库分表的集成
- **双写一致性**保证

## 架构设计

### 分库分表策略

```
分库策略: userId.hashCode() % 16 -> 数据库索引 (ds0 ~ ds15)
分表策略: userId.hashCode() % 2048 -> 表索引 (t_user_0000 ~ t_user_2047)

数据库命名: user_db_00, user_db_01, ..., user_db_15
表命名: t_user_0000, t_user_0001, ..., t_user_2047
```

### 雪花算法配置

- **机器ID**: 基于IP地址计算 (0-31)
- **数据中心ID**: 基于主机名计算 (0-31)
- **ID长度**: 64位长整型
- **并发性能**: 单机每秒可生成400万个ID

## 核心组件

### 1. 雪花算法ID生成器
```java
@Component
public class SnowflakeIdGenerator {
    // 基于Hutool的雪花算法实现
    // 自动获取机器ID和数据中心ID
}
```

### 2. 分库分表路由算法
```java
// 分库算法
public class UserDatabaseShardingAlgorithm implements StandardShardingAlgorithm<Long>

// 分表算法  
public class UserShardingAlgorithm implements StandardShardingAlgorithm<Long>
```

### 3. 数据源配置
```java
@Configuration
public class ShardingConfig {
    // 配置16个数据源
    // 配置分库分表规则
    // 集成ShardingSphere
}
```

## 数据库设计

### 表结构
```sql
CREATE TABLE t_user_xxxx (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    user_id BIGINT NOT NULL COMMENT '用户ID（雪花算法生成）',
    user_name VARCHAR(50) NOT NULL COMMENT '用户名',
    mobile_phone VARCHAR(20) NOT NULL COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    password VARCHAR(255) COMMENT '密码（加密后）',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '用户状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_time DATETIME COMMENT '最后登录时间',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    UNIQUE KEY uk_user_name (user_name),
    UNIQUE KEY uk_mobile_phone (mobile_phone),
    KEY idx_status (status),
    KEY idx_create_time (create_time),
    KEY idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## 部署指南

### 1. 环境准备
```bash
# MySQL 8.0+
# Redis 6.0+
# Java 8+
# Maven 3.6+
```

### 2. 数据库初始化
```bash
# 生成SQL脚本
cd scripts
python3 create_sharding_tables.py

# 执行SQL脚本
mysql -u root -p < init_sharding_databases.sql
```

### 3. 配置文件
```yaml
# application-sharding.yml
sharding:
  datasource:
    base-url: jdbc:mysql://localhost:3306/user_db
    username: root
    password: 123456
```

### 4. 启动应用
```bash
# 使用分库分表配置启动
mvn spring-boot:run -pl op-study-iface -Dspring.profiles.active=sharding
```

## API测试

### 1. 雪花算法ID生成测试
```bash
curl -X GET "http://localhost:8080/api/v1/sharding/snowflake/generate"
```

### 2. 批量创建用户测试
```bash
curl -X POST "http://localhost:8080/api/v1/sharding/users/batch?count=100"
```

### 3. 分库分表查询测试
```bash
curl -X GET "http://localhost:8080/api/v1/sharding/users/{userId}"
```

### 4. 路由信息查询
```bash
curl -X GET "http://localhost:8080/api/v1/sharding/route/info/{userId}"
```

## 性能优化

### 1. 连接池配置
```yaml
sharding:
  datasource:
    maximum-pool-size: 20    # 每个数据源最大连接数
    minimum-idle: 5          # 最小空闲连接数
    connection-timeout: 30000 # 连接超时时间
```

### 2. 缓存策略
- **用户信息缓存**: 按用户ID缓存，TTL 30分钟
- **分布式锁**: 防止缓存击穿
- **延迟双删**: 保证缓存一致性

### 3. 读写分离（可扩展）
```yaml
# 可配置主从读写分离
sharding:
  datasource:
    master: # 主库配置
    slave:  # 从库配置
```

## 监控指标

### 1. 分库分表指标
- 各数据库连接数
- SQL执行时间分布
- 路由命中率

### 2. 缓存指标  
- 缓存命中率
- 缓存更新频率
- 分布式锁竞争情况

### 3. ID生成指标
- ID生成速率
- 机器时钟偏移
- ID重复检测

## 故障处理

### 1. 数据库故障
- 单库故障不影响其他库
- 自动故障转移
- 数据恢复策略

### 2. 缓存故障
- 缓存降级策略
- 直接查询数据库
- 缓存重建机制

### 3. ID生成故障
- 时钟回拨处理
- 机器ID冲突处理
- 备用ID生成策略

## 扩容方案

### 1. 水平扩容
- 增加数据库数量
- 重新分布数据
- 在线迁移工具

### 2. 垂直扩容
- 增加数据库配置
- 优化索引结构
- 分区表策略

## 最佳实践

1. **避免跨库事务**: 尽量在单库内完成业务操作
2. **合理设计分片键**: 选择分布均匀的字段作为分片键
3. **监控数据倾斜**: 定期检查各分片的数据分布
4. **预留扩容空间**: 设计时考虑未来的扩容需求
5. **完善监控告警**: 建立完整的监控体系

## 注意事项

1. **分布式事务**: 跨库操作需要使用分布式事务
2. **全局查询**: 避免不带分片键的查询
3. **数据一致性**: 注意缓存与数据库的一致性
4. **运维复杂度**: 分库分表会增加运维复杂度
5. **数据迁移**: 扩容时需要考虑数据迁移方案 