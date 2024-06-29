package domain.context;

import com.rits.cloning.Cloner;
import domain.diff.DiffUtils;
import domain.diff.EntityDiff;
import domain.iface.Aggregate;
import domain.iface.Identifier;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xxs
 * @Date 2024/6/29 18:41
 */
public class RepoContext <T extends Aggregate<ID>, ID extends Identifier> {

    @Getter
    private final Class<? extends T> aggregateClass;

    private final Map<ID, T> aggregateMap = new ConcurrentHashMap<>();

    public RepoContext(Class<? extends T> aggregateClass) {
        this.aggregateClass = aggregateClass;
    }

    public void attach(T agg) {
        if (agg.getId() != null) {
            this.merge(agg);
        }
    }

    public void detach(T agg) {
        if (agg.getId() != null) {
            aggregateMap.remove(agg.getId());
        }
    }

    public EntityDiff detectChanges(T agg) {
        if (agg.getId() == null) {
            return EntityDiff.EMPTY;
        }
        T snapshot = aggregateMap.get(agg.getId());
        if (snapshot == null) {
            attach(agg);
        }
        return DiffUtils.diff(snapshot, agg);
    }

    public void merge(T agg) {
        if (agg.getId() != null) {
            Cloner cloner = new Cloner();
            T snapshot = cloner.deepClone(agg);
            aggregateMap.put(agg.getId(), snapshot);
        }
    }
}
