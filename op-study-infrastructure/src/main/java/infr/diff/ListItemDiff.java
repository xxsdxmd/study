package infr.diff;

import lombok.Value;

/**
 * @author xxs
 * @Date 2024/6/29 16:44
 */
@Value
public class ListItemDiff implements Diff {

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
