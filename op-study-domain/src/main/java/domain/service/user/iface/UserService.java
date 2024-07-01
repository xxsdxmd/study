package domain.service.user.iface;

import domain.entity.UserEntity;
import domain.entity.UserId;

/**
 * @author xxs
 * @Date 2024/7/1 22:30
 * 领域服务 这是核心的service
 */
public interface UserService {

    /**
     * 根据id查询user
     * @param userId
     * @return
     */
    UserEntity selectById(UserId userId);
}
