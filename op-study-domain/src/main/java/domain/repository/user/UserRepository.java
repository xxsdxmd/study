package domain.repository.user;

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



    public UserRepository() {
        super(UserEntity.class);
    }


    @Override
    protected void onInsert(UserEntity agg) {

    }

    @Override
    protected UserEntity onSelect(UserId userId) {
        return null;
    }

    @Override
    protected void onUpdate(UserEntity agg, EntityDiff diff) {

    }

    @Override
    protected void onDelete(UserEntity agg) {

    }
}
