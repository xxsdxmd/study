package domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("userId")
    private Long userId;

    /**
     * Jackson 反序列化构造器
     */
    @JsonCreator
    public static UserId of(@JsonProperty("userId") Long userId) {
        return new UserId(userId);
    }
}
