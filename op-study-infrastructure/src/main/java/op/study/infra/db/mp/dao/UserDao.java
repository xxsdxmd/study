package op.study.infra.db.mp.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author xxs
 * @Date 2024/6/30 15:00
 */
@Data
@EqualsAndHashCode
@TableName("t_user")
public class UserDao implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * userId
     */
    @TableField
    private Long userId;

    /**
     * 用户名
     */
    @TableField
    private String userName;

    /**
     * 手机号
     */
    @TableField
    private String mobilePhone;

    /**
     * 1 生效
     * 0 失效
     */
    @TableField(exist = false)
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
