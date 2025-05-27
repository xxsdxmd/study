package com.example.application.iface.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xxs
 * @Date 2024/6/30 21:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private Long id;

    @NotBlank(message = "userName is not null")
    private String userName;

    @NotBlank(message = "mobilePhone is not null")
    private String mobilePhone;
}
