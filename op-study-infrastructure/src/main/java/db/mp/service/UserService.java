package db.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import db.mp.dao.UserDao;

/**
 * @author xxs
 * @Date 2024/6/30 15:26
 */
public interface UserService extends IService<UserDao> {


    /**
     * create
     * @param userDao
     * @return
     */
    Long create(UserDao userDao);

    /**
     * diff 在domain里面已经做了
     * @param userDao
     */
    void updateUser(UserDao userDao);
}
