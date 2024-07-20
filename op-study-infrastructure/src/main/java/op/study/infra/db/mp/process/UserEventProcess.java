package op.study.infra.db.mp.process;

import op.study.infra.db.mp.event.UserUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author xxs
 * @Date 2024/6/30 16:38
 * 事件处理器
 * 例如发生mq 都可以在这边做
 * 上层业务线可以感知
 */
@Component
public class UserEventProcess {


    @EventListener
    public void handlerUserUpdateEvent(UserUpdateEvent userUpdateEvent) {

    }
}
