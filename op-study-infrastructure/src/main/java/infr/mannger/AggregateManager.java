package infr.mannger;

import infr.diff.EntityDiff;
import domain.marker.Aggregate;
import domain.marker.Identifier;


/**
 * @author xxs
 * @Date 2024/6/29 18:33
 * agg manager
 */
public interface AggregateManager <T extends Aggregate<ID>, ID extends Identifier> {

    void attach(T agg);

    void detach(T agg);

    void remove();

    EntityDiff detectChanges(T agg);

    void merge(T agg);

    static <T extends Aggregate<ID>, ID extends Identifier> AggregateManager<T, ID> newInstance(Class<T> aggregateClass) {
        return new ThreadLocalAggregateManager<>(aggregateClass);
    }
}
