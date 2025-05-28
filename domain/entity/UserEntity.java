/**
 * 用户实体
 */
@TableId(type = IdType.ASSIGN_ID)  // 使用雪花算法生成ID
private UserId userId; 