package op.study.infra.repo.user;

import op.study.infra.db.convert.UserAssembler;
import op.study.infra.db.mp.dao.UserDao;
import op.study.infra.db.mp.service.UserService;
import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.Repository;
import op.study.infra.diff.EntityDiff;
import op.study.infra.repo.RepositorySupport;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author xxs
 * @Date 2024/7/1 22:35
 */
@Service
public class UserRepositoryImpl extends RepositorySupport<UserEntity, UserId> implements Repository<UserEntity, UserId> {

    private final UserService userService;
    private final UserAssembler userAssembler;

    public UserRepositoryImpl(UserService userService, UserAssembler userAssembler) {
        super(UserEntity.class);
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @Override
    protected void onInsert(UserEntity agg) {
    }

    @Override
    protected UserEntity onSelect(UserId userId) {
        if (Objects.isNull(userId)) {
            throw new RuntimeException("userId is null");
        }
        UserDao userDao = userService.getById(userId.getUserId());
        return userAssembler.convertUserEntity(userDao);
    }

    @Override
    protected void onUpdate(UserEntity agg, EntityDiff diff) {

    }

    @Override
    protected void onDelete(UserEntity agg) {

    }
}
