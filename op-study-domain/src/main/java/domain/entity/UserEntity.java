package domain.entity;

import domain.iface.Aggregate;
import domain.iface.Entity;

/**
 * @author xxs
 * @Date 2024/6/29 1:02
 */
public class UserEntity implements Aggregate<UserId> {

    /**
     * 用户Id
     */
    private UserId userId;


    /**
     * 用户名
     */
    private String userName;


    @Override
    public UserId getId() {
        return userId;
    }
}
