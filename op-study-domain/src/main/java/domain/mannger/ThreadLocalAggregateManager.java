package domain.mannger;

import com.alibaba.ttl.TransmittableThreadLocal;
import domain.context.RepoContext;
import domain.diff.EntityDiff;
import domain.iface.Aggregate;
import domain.iface.Identifier;

/**
 * @author xxs
 * @Date 2024/6/29 18:32
 */
public class ThreadLocalAggregateManager <T extends Aggregate<ID>, ID extends Identifier> implements AggregateManager<T, ID> {

    private final TransmittableThreadLocal<RepoContext<T, ID>> context;

    public ThreadLocalAggregateManager(Class<? extends T> targetClass) {
        this.context = new TransmittableThreadLocal<RepoContext<T, ID>>() {

            @Override
            protected RepoContext<T,ID> initialValue() {
                return new RepoContext<>(targetClass);
            }

            @Override
            protected RepoContext<T, ID> childValue(RepoContext<T, ID> parentValue) {
                return initialValue();
            }
        };
    }

    @Override
    public void attach(T agg) {
        context.get().attach(agg);
    }

    @Override
    public void detach(T agg) {
        context.get().detach(agg);
    }

    @Override
    public void remove() {
        context.remove();
    }

    @Override
    public EntityDiff detectChanges(T agg) {
        return context.get().detectChanges(agg);
    }

    @Override
    public void merge(T agg) {
        context.get().merge(agg);
    }
}
