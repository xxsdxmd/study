package op.study.infra.diff;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xxs
 * @Date 2024/6/29 16:15
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class EntityDiff implements Diff, Iterable<Map .Entry<String, Diff>> {

    static public EntityDiff EMPTY = new EntityDiff();

    String fieldName;

    Class<?> fieldType;

    DiffType type = DiffType.UNCHANGED;

    Object oldValue;

    Object newValue;

    /**
     * key field
     * value diff
     */
    Map<String, Diff> diffMap = new HashMap<>();



    @Override
    public boolean isEmpty() {
        return diffMap.isEmpty();
    }


    @Override
    public Iterator<Map.Entry<String, Diff>> iterator() {
        return diffMap.entrySet().iterator();
    }


    public Diff getDiff(String fieldName) {
        return diffMap.get(fieldName);
    }

    public void addDiff(Diff diff) {
        diffMap.put(diff.getFieldName(), diff);
    }

}
