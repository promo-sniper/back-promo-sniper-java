package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    @NotBlank
    @JsonProperty
    private String name;

    @Email
    @JsonProperty
    private String email;


    @NotBlank
    @JsonProperty
    private String passwordHash;

    @JsonProperty
    private LocalDateTime createdDate;

    private Boolean isEnabled;

    @JsonProperty
    private LocalDateTime disabledDate;
}
