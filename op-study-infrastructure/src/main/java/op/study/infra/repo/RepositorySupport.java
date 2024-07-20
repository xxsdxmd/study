package op.study.infra.repo;


import op.study.infra.diff.EntityDiff;
import op.study.infra.mannger.AggregateManager;
import domain.marker.Aggregate;
import domain.marker.Identifier;
import domain.repository.Repository;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author xxs
 * @Date 2024/6/29 18:59
 */
public abstract class RepositorySupport<T extends Aggregate<ID>, ID extends Identifier> implements Repository<T, ID> {

    @Getter
    private final Class<T> targetClass;

    @Getter(AccessLevel.PROTECTED)
    private final AggregateManager<T, ID> aggregateManager;

    protected RepositorySupport(Class<T> targetClass) {
        this.targetClass = targetClass;
        this.aggregateManager = AggregateManager.newInstance(targetClass);
    }


    protected abstract void onInsert(T agg);
    protected abstract T onSelect(ID id);
    protected abstract void onUpdate(T agg, EntityDiff diff);
    protected abstract void onDelete(T agg);


    @Override
    public void attach(T agg) {
        this.aggregateManager.attach(agg);
    }

    @Override
    public void detach(T agg) {
        this.aggregateManager.detach(agg);
    }

    @Override
    public T find(ID id) {
        T aggregate = this.onSelect(id);
        if (aggregate != null) {
            this.attach(aggregate);
        }
        return aggregate;
    }

    @Override
    public void remove(T agg) {
        this.onDelete(agg);
        this.detach(agg);
    }

    @Override
    public void save(T agg) {
        if (agg.getId() == null) {
            this.onInsert(agg);
            this.attach(agg);
            return;
        }
        EntityDiff diff = aggregateManager.detectChanges(agg);
        if (diff.isEmpty()) {
            return;
        }
        this.onUpdate(agg, diff);
        aggregateManager.merge(agg);
    }
}
