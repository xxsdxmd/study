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
    @Mapping(target = "id", ignore = true)  // 忽略主键ID，由数据库自动生成
    @Mapping(target = "deleted", ignore = true)  // 忽略逻辑删除字段，使用默认值
    UserDao convertUserDao(UserEntity userEntity);

    /**
     * UserDao列表转UserEntity列表
     */
    default List<UserEntity> convertUserEntityList(List<UserDao> userDaoList) {
        if (userDaoList == null) {
            return null;
        }
        return userDaoList.stream()
                .map(this::convertUserEntity)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * UserEntity列表转UserDao列表
     */
    default List<UserDao> convertUserDaoList(List<UserEntity> userEntityList) {
        if (userEntityList == null) {
            return null;
        }
        return userEntityList.stream()
                .map(this::convertUserDao)
                .collect(java.util.stream.Collectors.toList());
    }

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
