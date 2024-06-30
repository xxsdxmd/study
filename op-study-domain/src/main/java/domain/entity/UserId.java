package domain.entity;

import domain.iface.Identifier;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xxs
 * @Date 2024/6/29 1:02
 */
@Data
public class UserId implements Identifier, Serializable {

    /**
     * userId
     */
    private Long userId;
}
