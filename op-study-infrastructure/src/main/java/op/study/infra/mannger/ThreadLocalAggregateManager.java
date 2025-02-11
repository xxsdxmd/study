package op.study.infra.mannger;


import com.alibaba.ttl.TransmittableThreadLocal;
import op.study.infra.context.RepoContext;
import op.study.infra.diff.EntityDiff;
import domain.marker.Aggregate;
import domain.marker.Identifier;

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
