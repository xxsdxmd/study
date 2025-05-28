package op.study.infra.db.mp.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xxs
 * @Date 2024/6/30 15:00
 * 用户数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class UserDao implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（业务ID，雪花算法生成）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 手机号
     */
    @TableField("mobile_phone")
    private String mobilePhone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 密码（加密后）
     */
    @TableField("password")
    private String password;

    /**
     * 用户状态
     * 0: 未激活, 1: 激活, 2: 已禁用, 3: 已删除
     */
    @TableField("status")
    @Builder.Default
    private Integer status = 0;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    @Builder.Default
    private Long version = 0L;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    @Builder.Default
    private Integer deleted = 0;

    /**
     * 初始化默认值
     */
    public void init() {
        if (this.status == null) {
            this.status = 0; // 默认未激活
        }
        if (this.version == null) {
            this.version = 0L;
        }
        if (this.deleted == null) {
            this.deleted = 0;
        }
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now();
        }
        if (this.updateTime == null) {
            this.updateTime = LocalDateTime.now();
        }
    }

    /**
     * 更新数据
     */
    public void updateFrom(UserDao userDao) {
        if (userDao == null) {
            return;
        }
        if (userDao.getUserName() != null) {
            this.userName = userDao.getUserName();
        }
        if (userDao.getMobilePhone() != null) {
            this.mobilePhone = userDao.getMobilePhone();
        }
        if (userDao.getEmail() != null) {
            this.email = userDao.getEmail();
        }
        if (userDao.getPassword() != null) {
            this.password = userDao.getPassword();
        }
        if (userDao.getStatus() != null) {
            this.status = userDao.getStatus();
        }
        this.updateTime = LocalDateTime.now();
    }
}
