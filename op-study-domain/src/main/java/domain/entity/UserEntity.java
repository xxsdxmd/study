package domain.entity;

import domain.iface.Aggregate;
import lombok.Data;

/**
 * @author xxs
 * @Date 2024/6/29 1:02
 */
@Data
public class UserEntity implements Aggregate<UserId> {

    /**
     * 用户Id
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


    @Override
    public UserId getId() {
        return userId;
    }
}
