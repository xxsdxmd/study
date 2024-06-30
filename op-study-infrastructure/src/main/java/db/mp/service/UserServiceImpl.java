package db.mp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.opstudycommon.enums.EnumUserType;
import com.example.opstudycommon.support.EntityOperations;
import db.mp.dao.UserDao;
import db.mp.event.UserUpdateEvent;
import db.mp.mapper.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xxs
 * @Date 2024/6/30 15:27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDao> implements UserService {

    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Long create(UserDao userDao) {
        return EntityOperations.doCreate(getBaseMapper())
                .create(() -> userDao)
                .update(UserDao::init)
                .executor().map(UserDao::getUserId).orElse(null);
    }

    @Override
    @Transactional
    public void updateUser(UserDao userDao) {
        EntityOperations
                .doUpdate(getBaseMapper())
                .load(() -> getById(userDao.getUserId()))
                .update(e -> e.convertDao(userDao))
                .successHook(e -> eventPublisher.publishEvent(new UserUpdateEvent(e, EnumUserType.UPDATE)))
                .executor();
    }


}
