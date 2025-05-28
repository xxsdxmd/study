package domain.service.user;

import domain.entity.UserId;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户ID生成服务
 */
public interface UserIdGeneratorService {

    /**
     * 生成新的用户ID
     * @return 用户ID
     */
    UserId generateUserId();

    /**
     * 根据长整型值创建用户ID
     * @param id 长整型ID
     * @return 用户ID
     */
    UserId createUserId(Long id);
} 