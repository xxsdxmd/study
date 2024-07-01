package domain.repository;

import domain.marker.Aggregate;
import domain.marker.Identifier;


/**
 * @author xxs
 * @Date 2024/6/29 0:56
 * repository
 * 实现由infra实现
 */
public interface Repository<T extends Aggregate<ID>, ID extends Identifier> {

    /**
     * 将一个Aggregate附属到一个Repository，让它变为可追踪
     * @param agg
     */
    void attach(T agg);

    /**
     * 解除一个Aggregate的追踪
     * @param agg
     */
    void detach(T agg);

    /**
     * id find Agg
     * @param id
     * @return
     */
    T find(ID id);

    /**
     * 将一个Aggregate从Repository移除
     * @param agg
     */
    void remove(T agg);

    /**
     * 保存一个Agg
     * @param agg
     */
    void save(T agg);

}
