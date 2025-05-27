package domain.repository;

import domain.entity.UserEntity;
import domain.entity.UserId;

import java.util.List;
import java.util.Optional;

/**
 * @author xxs
 * @Date 2024/7/1 22:35
 * 用户仓储接口 - 领域层定义
 */
public interface UserRepository extends Repository<UserEntity, UserId> {

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户实体
     */
    Optional<UserEntity> findByUserName(String userName);

    /**
     * 根据手机号查询用户
     * @param mobilePhone 手机号
     * @return 用户实体
     */
    Optional<UserEntity> findByMobilePhone(String mobilePhone);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户实体
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     * @param userName 用户名
     * @return 是否存在
     */
    boolean existsByUserName(String userName);

    /**
     * 检查手机号是否已存在
     * @param mobilePhone 手机号
     * @return 是否存在
     */
    boolean existsByMobilePhone(String mobilePhone);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 分页查询用户列表
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 用户列表
     */
    List<UserEntity> findUsersByPage(int pageNum, int pageSize);

    /**
     * 根据状态查询用户列表
     * @param status 用户状态
     * @return 用户列表
     */
    List<UserEntity> findUsersByStatus(Integer status);
} 