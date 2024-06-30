package domain.assembler;

import db.mp.dao.UserDao;
import domain.entity.UserEntity;
import org.mapstruct.Mapper;

/**
 * @author xxs
 * @Date 2024/6/30 20:36
 */
@Mapper(componentModel = "spring")
public interface UserConvert {


    UserEntity convertUserEntity(UserDao userDao);

}
