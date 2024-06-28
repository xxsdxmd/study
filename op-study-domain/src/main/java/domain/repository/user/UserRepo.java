package domain.repository.user;

import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.Repository;
import lombok.NonNull;

/**
 * @author xxs
 * @Date 2024/6/29 1:05
 */
public class UserRepo implements Repository<UserEntity, UserId> {

    @Override
    public void attach(UserEntity agg) {

    }

    @Override
    public void detach(UserEntity agg) {

    }

    @Override
    public UserEntity find(UserId userId) {
        return null;
    }

    @Override
    public void remove(UserEntity agg) {

    }

    @Override
    public void save(UserEntity agg) {

    }
}
