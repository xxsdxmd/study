package op.study.infra.diff;


import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author xxs
 * @Date 2024/6/29 16:15
 */
@Getter
public class ListDiff implements Diff, Iterable<Diff> {
    String fieldName;

    Class<?> fieldType;

    DiffType type;

    Object oldValue;

    Object newValue;

    private List<Diff> diffList = new ArrayList<>();

    private ListDiff(){}

    public static ListDiff empty(Field field) {
        ListDiff listDiff = new ListDiff();
        listDiff.fieldName = field.getName();
        listDiff.fieldType = field.getType();
        listDiff.type = DiffType.UNCHANGED;
        return listDiff;
    }


    public static ListDiff from(Field field, Object oldValue, Object newValue) {
        ListDiff listDiff = new ListDiff();
        listDiff.fieldName = field.getName();
        listDiff.fieldType = field.getType();
        listDiff.type = DiffType.UNCHANGED;
        listDiff.oldValue = oldValue;
        listDiff.newValue = newValue;
        return listDiff;
    }

    public static ListDiff fromList(Field field, Object oldValue, Object newValue, List<Diff> diffList) {
        ListDiff diff = from(field, oldValue, newValue);
        diff.diffList = diffList;
        return diff;
    }


    @Override
    public boolean isEmpty() {
        return diffList.isEmpty();
    }

    @Override
    public Iterator<Diff> iterator() {
        return diffList.iterator();
    }
}
