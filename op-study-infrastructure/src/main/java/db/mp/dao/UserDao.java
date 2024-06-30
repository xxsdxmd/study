package db.mp.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author xxs
 * @Date 2024/6/30 15:00
 */
@Data
@EqualsAndHashCode
public class UserDao implements Serializable {

    /**
     * userId
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 1 生效
     * 0 失效
     */
    private Integer status;


    /**
     * init
     */
    public void init() {
        setStatus(1);
    }


    /**
     * 这块其实应该是把
     * @param userDao
     */
    public void convertDao(UserDao userDao) {
        this.userName = userDao.getUserName();
        this.mobilePhone = userDao.getMobilePhone();
    }
}
