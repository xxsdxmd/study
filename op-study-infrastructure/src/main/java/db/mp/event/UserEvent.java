package db.mp.event;


/**
 * @author xxs
 * @Date 2024/6/30 16:16
 * 领域事件
 */
public interface UserEvent<T> {

    T createEvent();
}
