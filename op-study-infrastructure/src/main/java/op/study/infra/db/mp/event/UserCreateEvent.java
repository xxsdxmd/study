package op.study.infra.db.mp.event;

import com.example.opstudycommon.enums.EnumUserType;
import lombok.Getter;
import op.study.infra.db.mp.dao.UserDao;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreateEvent extends ApplicationEvent {
    private final UserDao userDao;
    private final EnumUserType userType;

    public UserCreateEvent(UserDao userDao, EnumUserType userType) {
        super(userDao);
        this.userDao = userDao;
        this.userType = userType;
    }
}
