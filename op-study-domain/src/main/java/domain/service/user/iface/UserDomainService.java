package domain.service.user.iface;

import domain.entity.UserEntity;
import domain.entity.UserId;

import java.util.List;
import java.util.Optional;

/**
 * @author xxs
 * @Date 2024/7/1 22:30
 * 用户领域服务 - 核心业务逻辑
 */
public interface UserDomainService {

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户实体
     */
    UserEntity selectById(UserId userId);

    /**
     * 根据ID查询用户（Optional）
     * @param userId 用户ID
     * @return Optional用户实体
     */
    Optional<UserEntity> findById(UserId userId);

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
     * 创建新用户
     * @param userEntity 用户实体
     * @return 创建后的用户实体
     */
    UserEntity createUser(UserEntity userEntity);

    /**
     * 更新用户信息
     * @param userEntity 用户实体
     * @return 更新后的用户实体
     */
    UserEntity updateUser(UserEntity userEntity);

    /**
     * 删除用户（软删除）
     * @param userId 用户ID
     */
    void deleteUser(UserId userId);

    /**
     * 激活用户
     * @param userId 用户ID
     */
    void activateUser(UserId userId);

    /**
     * 停用用户
     * @param userId 用户ID
     */
    void deactivateUser(UserId userId);

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
     * 用户登录验证
     * @param loginName 登录名（用户名/手机号/邮箱）
     * @param password 密码
     * @return 用户实体
     */
    Optional<UserEntity> authenticate(String loginName, String password);

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     */
    void updateLastLoginTime(UserId userId);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(UserId userId, String oldPassword, String newPassword);

    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     */
    void resetPassword(UserId userId, String newPassword);

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
