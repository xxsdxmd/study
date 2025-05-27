package op.study.infra.db.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import op.study.infra.db.mp.dao.UserDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/30 15:02
 * 用户数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDao> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM t_user WHERE user_name = #{userName} AND deleted = 0")
    UserDao findByUserName(@Param("userName") String userName);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM t_user WHERE mobile_phone = #{mobilePhone} AND deleted = 0")
    UserDao findByMobilePhone(@Param("mobilePhone") String mobilePhone);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM t_user WHERE email = #{email} AND deleted = 0")
    UserDao findByEmail(@Param("email") String email);

    /**
     * 根据业务用户ID查询用户
     */
    @Select("SELECT * FROM t_user WHERE user_id = #{userId} AND deleted = 0")
    UserDao findByUserId(@Param("userId") Long userId);

    /**
     * 根据状态查询用户列表
     */
    @Select("SELECT * FROM t_user WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    List<UserDao> findByStatus(@Param("status") Integer status);

    /**
     * 分页查询用户列表
     */
    @Select("SELECT * FROM t_user WHERE deleted = 0 ORDER BY create_time DESC")
    IPage<UserDao> selectUserPage(Page<UserDao> page);

    /**
     * 更新最后登录时间
     */
    @Update("UPDATE t_user SET last_login_time = #{lastLoginTime}, update_time = #{updateTime} WHERE user_id = #{userId} AND deleted = 0")
    int updateLastLoginTime(@Param("userId") Long userId, 
                           @Param("lastLoginTime") LocalDateTime lastLoginTime,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据用户名检查是否存在
     */
    @Select("SELECT COUNT(1) FROM t_user WHERE user_name = #{userName} AND deleted = 0")
    int countByUserName(@Param("userName") String userName);

    /**
     * 根据手机号检查是否存在
     */
    @Select("SELECT COUNT(1) FROM t_user WHERE mobile_phone = #{mobilePhone} AND deleted = 0")
    int countByMobilePhone(@Param("mobilePhone") String mobilePhone);

    /**
     * 根据邮箱检查是否存在
     */
    @Select("SELECT COUNT(1) FROM t_user WHERE email = #{email} AND deleted = 0")
    int countByEmail(@Param("email") String email);

    /**
     * 批量查询用户
     */
    @Select("<script>" +
            "SELECT * FROM t_user WHERE user_id IN " +
            "<foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>" +
            " AND deleted = 0" +
            "</script>")
    List<UserDao> findByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 根据关键字模糊查询用户
     */
    @Select("SELECT * FROM t_user WHERE " +
            "(user_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR mobile_phone LIKE CONCAT('%', #{keyword}, '%') " +
            "OR email LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND deleted = 0 " +
            "ORDER BY create_time DESC")
    List<UserDao> searchUsers(@Param("keyword") String keyword);
}
