package domain.service.user.iface.impl;

import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.Repository;
import domain.service.user.iface.UserService;
import org.springframework.stereotype.Service;

/**
 * @author xxs
 * @Date 2024/7/1 22:31
 */
@Service
public class UserServiceImpl implements UserService {

    private final Repository<UserEntity, UserId> userRepository;

    public UserServiceImpl(Repository<UserEntity, UserId> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity selectById(UserId userId) {
        return userRepository.find(userId);
    }
}
