package domain.entity;

import com.example.opstudycommon.enums.UserStatus;
import domain.marker.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xxs
 * @Date 2024/6/29 1:02
 * 用户聚合根
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Aggregate<UserId>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private UserId userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码（加密后）
     */
    private String password;

    /**
     * 用户状态
     */
    @Builder.Default
    private UserStatus status = UserStatus.INACTIVE;

    /**
     * 创建时间
     */
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Builder.Default
    private LocalDateTime updateTime = LocalDateTime.now();

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 版本号（乐观锁）
     */
    @Builder.Default
    private Long version = 0L;

    @Override
    public UserId getId() {
        return userId;
    }

    /**
     * 激活用户
     */
    public void activate() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("已删除的用户无法激活");
        }
        this.status = UserStatus.ACTIVE;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 停用用户
     */
    public void deactivate() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("已删除的用户无法停用");
        }
        this.status = UserStatus.DISABLED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 删除用户（软删除）
     */
    public void delete() {
        this.status = UserStatus.DELETED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 更新最后登录时间
     */
    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(String userName, String email) {
        if (userName != null && !userName.trim().isEmpty()) {
            this.userName = userName.trim();
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim();
        }
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 修改密码
     */
    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        this.password = newPassword;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 检查用户是否可用
     */
    public boolean isAvailable() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * 检查用户是否已删除
     */
    public boolean isDeleted() {
        return this.status == UserStatus.DELETED;
    }

    /**
     * 验证用户数据完整性
     */
    public void validate() {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (mobilePhone == null || mobilePhone.trim().isEmpty()) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        // 可以添加更多验证逻辑
    }
}
