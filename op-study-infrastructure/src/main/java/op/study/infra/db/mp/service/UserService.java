package op.study.infra.db.mp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import op.study.infra.db.mp.dao.UserDao;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/30 15:26
 * 用户数据服务接口
 */
public interface UserService extends IService<UserDao> {

    /**
     * 创建用户
     * @param userDao 用户数据对象
     * @return 用户ID
     */
    Long create(UserDao userDao);

    /**
     * 更新用户信息
     * @param userDao 用户数据对象
     */
    void updateUser(UserDao userDao);

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户数据对象
     */
    UserDao findByUserName(String userName);

    /**
     * 根据手机号查询用户
     * @param mobilePhone 手机号
     * @return 用户数据对象
     */
    UserDao findByMobilePhone(String mobilePhone);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户数据对象
     */
    UserDao findByEmail(String email);

    /**
     * 根据业务用户ID查询用户
     * @param userId 业务用户ID
     * @return 用户数据对象
     */
    UserDao findByUserId(Long userId);

    /**
     * 根据状态查询用户列表
     * @param status 用户状态
     * @return 用户列表
     */
    List<UserDao> findByStatus(Integer status);

    /**
     * 分页查询用户列表
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    IPage<UserDao> findUsersByPage(int pageNum, int pageSize);

    /**
     * 更新最后登录时间
     * @param userId 用户ID
     * @param lastLoginTime 最后登录时间
     * @return 更新结果
     */
    boolean updateLastLoginTime(Long userId, LocalDateTime lastLoginTime);

    /**
     * 检查用户名是否存在
     * @param userName 用户名
     * @return 是否存在
     */
    boolean existsByUserName(String userName);

    /**
     * 检查手机号是否存在
     * @param mobilePhone 手机号
     * @return 是否存在
     */
    boolean existsByMobilePhone(String mobilePhone);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 批量查询用户
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<UserDao> findByUserIds(List<Long> userIds);

    /**
     * 根据关键字搜索用户
     * @param keyword 关键字
     * @return 用户列表
     */
    List<UserDao> searchUsers(String keyword);

    /**
     * 软删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean softDeleteUser(Long userId);

    /**
     * 激活用户
     * @param userId 用户ID
     * @return 激活结果
     */
    boolean activateUser(Long userId);

    /**
     * 停用用户
     * @param userId 用户ID
     * @return 停用结果
     */
    boolean deactivateUser(Long userId);
}
