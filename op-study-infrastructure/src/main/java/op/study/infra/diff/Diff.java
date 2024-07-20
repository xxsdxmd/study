package op.study.infra.diff;

/**
 * @author xxs
 * @Date 2024/6/29 16:15
 * diff 比较entity的diff
 */
public interface Diff {

    String getFieldName();

    Class<?>  getFieldType();

    DiffType getType();

    Object getOldValue();

    Object getNewValue();

    boolean isEmpty();
}
