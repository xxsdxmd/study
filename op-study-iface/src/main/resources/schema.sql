-- H2 数据库初始化脚本

-- 创建用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID（业务ID）',
  user_name VARCHAR(50) NOT NULL COMMENT '用户名',
  mobile_phone VARCHAR(20) NOT NULL COMMENT '手机号',
  email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  password VARCHAR(255) DEFAULT NULL COMMENT '密码（加密后）',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '用户状态：1-激活，0-未激活，-1-已禁用，-2-已删除',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  last_login_time TIMESTAMP DEFAULT NULL COMMENT '最后登录时间',
  version BIGINT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0-未删除，1-已删除'
);

-- 创建唯一索引
CREATE UNIQUE INDEX uk_user_id ON t_user(user_id);
CREATE UNIQUE INDEX uk_user_name ON t_user(user_name);
CREATE UNIQUE INDEX uk_mobile_phone ON t_user(mobile_phone);
CREATE UNIQUE INDEX uk_email ON t_user(email);

-- 创建普通索引
CREATE INDEX idx_status ON t_user(status);
CREATE INDEX idx_create_time ON t_user(create_time);
CREATE INDEX idx_deleted ON t_user(deleted);
CREATE INDEX idx_user_status_create_time ON t_user(status, create_time);
CREATE INDEX idx_user_update_time ON t_user(update_time);

-- 创建用户操作日志表
DROP TABLE IF EXISTS t_user_operation_log;
CREATE TABLE t_user_operation_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
  operation_desc VARCHAR(255) DEFAULT NULL COMMENT '操作描述',
  operator_id BIGINT DEFAULT NULL COMMENT '操作人ID',
  operator_name VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
  request_id VARCHAR(100) DEFAULT NULL COMMENT '请求ID',
  ip_address VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  user_agent VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 创建用户会话表
DROP TABLE IF EXISTS t_user_session;
CREATE TABLE t_user_session (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  session_id VARCHAR(100) NOT NULL COMMENT '会话ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  login_time TIMESTAMP NOT NULL COMMENT '登录时间',
  last_access_time TIMESTAMP NOT NULL COMMENT '最后访问时间',
  ip_address VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  user_agent VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '会话状态：1-活跃，0-已过期',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 创建会话表索引
CREATE UNIQUE INDEX uk_session_id ON t_user_session(session_id);
CREATE INDEX idx_session_user_id ON t_user_session(user_id);
CREATE INDEX idx_session_status ON t_user_session(status);
CREATE INDEX idx_last_access_time ON t_user_session(last_access_time); 