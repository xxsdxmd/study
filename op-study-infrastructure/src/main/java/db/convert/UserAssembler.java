package db.convert;

import db.mp.dao.UserDao;
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
