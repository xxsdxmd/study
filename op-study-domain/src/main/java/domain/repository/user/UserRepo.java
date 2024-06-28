package domain.repository.user;

import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.Repository;
import lombok.NonNull;

/**
 * @author xxs
 * @Date 2024/6/29 1:05
 */
public class UserRepo extends Repository<UserEntity, UserId> {

    @Override
    public void attach(@NonNull UserEntity agg) {

    }

    @Override
    public void detach(@NonNull UserEntity agg) {

    }

    @Override
    public UserEntity find(@NonNull UserId userId) {
        return null;
    }

    @Override
    public void remove(@NonNull UserEntity agg) {

    }

    @Override
    public void save(@NonNull UserEntity agg) {

    }
}
