package db.mp.event;

import com.example.opstudycommon.enums.EnumUserType;
import db.mp.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xxs
 * @Date 2024/6/30 16:33
 */
@AllArgsConstructor
@Data
public class UserUpdateEvent {

    private UserDao userDao;
    private EnumUserType userType;
}
