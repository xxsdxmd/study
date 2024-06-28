package domain.iface;

/**
 * @author xxs
 * @Date 2024/6/29 0:54
 * 唯一标识 Entity
 */
public interface Identifiable <ID extends Identifier>{

    /**
     * getId
     * @return
     */
    ID getId();
}
