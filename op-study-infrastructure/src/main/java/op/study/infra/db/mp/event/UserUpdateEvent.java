package op.study.infra.db.mp.event;

import com.example.opstudycommon.enums.EnumUserType;
import op.study.infra.db.mp.dao.UserDao;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xxs
 * @Date 2024/6/30 16:33
 */
@Getter
public class UserUpdateEvent extends ApplicationEvent {

    private final UserDao userDao;
    private final EnumUserType userType;

    public UserUpdateEvent(UserDao userDao, EnumUserType userType) {
        super(userDao);
        this.userDao = userDao;
        this.userType = userType;
    }
}
