package domain.entity;

import domain.marker.Identifier;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xxs
 * @Date 2024/6/29 1:02
 */
@Data
@AllArgsConstructor
public class UserId implements Identifier, Serializable {

    /**
     * userId
     */
    private Long userId;
}
