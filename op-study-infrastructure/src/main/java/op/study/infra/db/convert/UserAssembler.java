package op.study.infra.db.convert;

import op.study.infra.db.mp.dao.UserDao;
import domain.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author xxs
 * @Date 2024/7/1 22:41
 */
@Mapper(componentModel = "spring")
public interface UserAssembler {


    UserEntity convertUserEntity(UserDao userDao);
}
