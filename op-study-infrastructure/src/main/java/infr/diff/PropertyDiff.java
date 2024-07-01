package infr.diff;

import lombok.Value;

/**
 * @author xxs
 * @Date 2024/6/29 17:05
 */
@Value
public class PropertyDiff implements Diff {

    String fieldName;

    Class<?> fieldType;

    DiffType type;

    Object oldValue;

    Object newValue;

    @Override
    public boolean isEmpty() {
        return false;
    }
}
