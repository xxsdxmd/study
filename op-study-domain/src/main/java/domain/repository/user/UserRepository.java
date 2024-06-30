package domain.repository.user;

import db.mp.dao.UserDao;
import db.mp.service.UserService;
import domain.assembler.UserConvert;
import domain.diff.EntityDiff;
import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.RepositorySupport;
import org.springframework.stereotype.Service;

/**
 * @author xxs
 * @Date 2024/6/29 1:05
 * 这层其实可以不用再搞一个
 * UserService proxy了
 */
@Service
public class UserRepository extends RepositorySupport<UserEntity, UserId> {

    private final UserService userService;
    private final UserConvert userConvert;

    public UserRepository(UserService userService, UserConvert userConvert) {
        super(UserEntity.class);
        this.userService = userService;
        this.userConvert = userConvert;
    }


    @Override
    protected void onInsert(UserEntity agg) {
    }

    @Override
    protected UserEntity onSelect(UserId userId) {
        UserDao userDao = userService.getById(userId.getUserId());
        return userConvert.convertUserEntity(userDao);
    }

    @Override
    protected void onUpdate(UserEntity agg, EntityDiff diff) {

    }

    @Override
    protected void onDelete(UserEntity agg) {

    }
}
