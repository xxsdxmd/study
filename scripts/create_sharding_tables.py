#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
分库分表数据库初始化脚本生成器
生成16个数据库，每个数据库128张表，总共2048张表
"""

def generate_create_table_sql():
    """生成创建表的SQL"""
    sql_lines = []
    sql_lines.append("-- 分库分表用户表创建脚本")
    sql_lines.append("-- 16个数据库，每个数据库128张表，总共2048张表")
    sql_lines.append("")
    
    # 为每个数据库创建128张表
    for db_index in range(16):
        db_name = f"study_db_{db_index:02d}"
        sql_lines.append(f"\n-- 数据库 {db_name}")
        sql_lines.append(f"USE {db_name};")
        
        for table_index in range(128):
            global_table_index = db_index * 128 + table_index
            table_name = f"t_user_{global_table_index:04d}"
            
            table_sql = f"""
CREATE TABLE IF NOT EXISTS {table_name} (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    user_id BIGINT NOT NULL COMMENT '用户ID（雪花算法生成）',
    user_name VARCHAR(50) NOT NULL COMMENT '用户名',
    mobile_phone VARCHAR(20) NOT NULL COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    password VARCHAR(255) COMMENT '密码（加密后）',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '用户状态：0-未激活，1-激活，2-禁用，3-删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    UNIQUE KEY uk_user_name (user_name),
    UNIQUE KEY uk_mobile_phone (mobile_phone),
    KEY idx_status (status),
    KEY idx_create_time (create_time),
    KEY idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表_{global_table_index:04d}';"""
            
            sql_lines.append(table_sql)
    
    return "\n".join(sql_lines)

def main():
    """主函数"""
    print("正在生成分库分表SQL脚本...")
    
    # 生成完整的SQL脚本
    full_sql = generate_create_table_sql()
    
    # 写入文件
    with open("init_sharding_tables.sql", "w", encoding="utf-8") as f:
        f.write(full_sql)
    
    print("SQL脚本生成完成: init_sharding_tables.sql")
    print("包含:")
    print("- 16个数据库 (study_db_00 到 study_db_15)")
    print("- 每个数据库128张表")
    print("- 总共2048张表")
    print("\n执行命令:")
    print("mysql -u root -p234234 < init_sharding_tables.sql")

if __name__ == "__main__":
    main() 