package com.wirehood.adminservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO implements Serializable {

    private String emailAddress;

    private Long id;

    private String password;

    private Character status;

    private Integer loginAttempt;
}
