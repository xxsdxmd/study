-- 用户域DDD项目数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `user_domain` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `user_domain`;

-- 创建用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID（业务ID）',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `mobile_phone` varchar(20) NOT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(255) DEFAULT NULL COMMENT '密码（加密后）',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户状态：1-激活，0-未激活，-1-已禁用，-2-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `version` bigint(20) NOT NULL DEFAULT '0' COMMENT '版本号（乐观锁）',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_user_name` (`user_name`),
  UNIQUE KEY `uk_mobile_phone` (`mobile_phone`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入测试数据
INSERT INTO `t_user` (`user_id`, `user_name`, `mobile_phone`, `email`, `password`, `status`, `create_time`, `update_time`) VALUES
(1001, 'admin', '13800138000', 'admin@example.com', 'admin123', 1, NOW(), NOW()),
(1002, 'user1', '13800138001', 'user1@example.com', 'user123', 1, NOW(), NOW()),
(1003, 'user2', '13800138002', 'user2@example.com', 'user123', 0, NOW(), NOW()),
(1004, 'user3', '13800138003', 'user3@example.com', 'user123', -1, NOW(), NOW());

-- 创建用户操作日志表
DROP TABLE IF EXISTS `t_user_operation_log`;
CREATE TABLE `t_user_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_desc` varchar(255) DEFAULT NULL COMMENT '操作描述',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `request_id` varchar(100) DEFAULT NULL COMMENT '请求ID',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_request_id` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户操作日志表';

-- 创建用户会话表
DROP TABLE IF EXISTS `t_user_session`;
CREATE TABLE `t_user_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` varchar(100) NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `last_access_time` datetime NOT NULL COMMENT '最后访问时间',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '会话状态：1-活跃，0-已过期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_id` (`session_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_last_access_time` (`last_access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- 创建索引优化查询性能
CREATE INDEX `idx_user_status_create_time` ON `t_user` (`status`, `create_time`);
CREATE INDEX `idx_user_update_time` ON `t_user` (`update_time`);

-- 创建视图：活跃用户视图
CREATE OR REPLACE VIEW `v_active_users` AS
SELECT 
    `user_id`,
    `user_name`,
    `mobile_phone`,
    `email`,
    `create_time`,
    `last_login_time`
FROM `t_user`
WHERE `status` = 1 AND `deleted` = 0;

-- 创建存储过程：用户统计
DELIMITER $$
CREATE PROCEDURE `sp_user_statistics`()
BEGIN
    SELECT 
        COUNT(*) as total_users,
        SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as active_users,
        SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as inactive_users,
        SUM(CASE WHEN status = -1 THEN 1 ELSE 0 END) as disabled_users,
        SUM(CASE WHEN deleted = 1 THEN 1 ELSE 0 END) as deleted_users
    FROM t_user;
END$$
DELIMITER ;

-- 创建触发器：用户状态变更日志
DELIMITER $$
CREATE TRIGGER `tr_user_status_change` 
AFTER UPDATE ON `t_user`
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO `t_user_operation_log` (
            `user_id`, 
            `operation_type`, 
            `operation_desc`, 
            `create_time`
        ) VALUES (
            NEW.user_id,
            'STATUS_CHANGE',
            CONCAT('状态从 ', OLD.status, ' 变更为 ', NEW.status),
            NOW()
        );
    END IF;
END$$
DELIMITER ;

-- 提交事务
COMMIT; 