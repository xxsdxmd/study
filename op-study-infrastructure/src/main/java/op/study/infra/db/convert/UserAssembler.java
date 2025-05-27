package op.study.infra.db.convert;

import com.example.opstudycommon.enums.UserStatus;
import domain.entity.UserEntity;
import domain.entity.UserId;
import op.study.infra.db.mp.dao.UserDao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * @author xxs
 * @Date 2024/7/1 22:41
 * 用户实体转换器
 */
@Mapper(componentModel = "spring")
public interface UserAssembler {

    /**
     * UserDao转UserEntity
     */
    @Mapping(source = "userId", target = "userId", qualifiedByName = "longToUserId")
    @Mapping(source = "status", target = "status", qualifiedByName = "integerToUserStatus")
    UserEntity convertUserEntity(UserDao userDao);

    /**
     * UserEntity转UserDao
     */
    @Mapping(source = "userId", target = "userId", qualifiedByName = "userIdToLong")
    @Mapping(source = "status", target = "status", qualifiedByName = "userStatusToInteger")
    UserDao convertUserDao(UserEntity userEntity);

    /**
     * UserDao列表转UserEntity列表
     */
    List<UserEntity> convertUserEntityList(List<UserDao> userDaoList);

    /**
     * UserEntity列表转UserDao列表
     */
    List<UserDao> convertUserDaoList(List<UserEntity> userEntityList);

    /**
     * Long转UserId
     */
    @Named("longToUserId")
    default UserId longToUserId(Long userId) {
        return userId != null ? new UserId(userId) : null;
    }

    /**
     * UserId转Long
     */
    @Named("userIdToLong")
    default Long userIdToLong(UserId userId) {
        return userId != null ? userId.getUserId() : null;
    }

    /**
     * Integer转UserStatus
     */
    @Named("integerToUserStatus")
    default UserStatus integerToUserStatus(Integer status) {
        return status != null ? UserStatus.getByCode(status) : null;
    }

    /**
     * UserStatus转Integer
     */
    @Named("userStatusToInteger")
    default Integer userStatusToInteger(UserStatus status) {
        return status != null ? status.getCode() : null;
    }
}
