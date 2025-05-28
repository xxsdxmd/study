package op.study.infra.context;

import op.study.infra.diff.DiffUtils;
import op.study.infra.diff.EntityDiff;
import domain.marker.Aggregate;
import domain.marker.Identifier;
import lombok.Getter;

import java.io.*;
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
            return EntityDiff.EMPTY;
        }
        return DiffUtils.diff(snapshot, agg);
    }

    public void merge(T agg) {
        if (agg.getId() != null) {
            try {
                // 使用序列化方式进行深拷贝
                T snapshot = deepCopyBySerialization(agg);
                aggregateMap.put(agg.getId(), snapshot);
                System.out.println("成功创建快照: " + agg.getId());
            } catch (Exception e) {
                // 如果序列化失败，记录错误但不影响主流程
                System.err.println("创建快照失败，跳过快照功能: " + e.getMessage());
                // 可以选择存储引用作为备选方案
                aggregateMap.put(agg.getId(), agg);
            }
        }
    }

    /**
     * 使用序列化方式进行深拷贝
     */
    @SuppressWarnings("unchecked")
    private T deepCopyBySerialization(T original) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.flush();
        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        T copy = (T) ois.readObject();
        ois.close();
        
        return copy;
    }
}
