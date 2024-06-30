package db.mp.event;

import com.example.opstudycommon.enums.EnumUserType;
import db.mp.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xxs
 * @Date 2024/6/30 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEvent implements UserEvent {

    private UserDao userDao;
    private EnumUserType userType;

    @Override
    public Object createEvent() {
        return new UserUpdateEvent(userDao, userType);
    }
}
