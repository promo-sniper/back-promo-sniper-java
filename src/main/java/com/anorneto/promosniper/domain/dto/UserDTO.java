package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {
    @JsonProperty
    private int id;

    @NotBlank
    @JsonProperty
    private String name;

    @Email
    @JsonProperty
    private String email;


    @NotBlank
    @JsonProperty
    private String passwordHash;

    @NotNull
    @JsonProperty
    private LocalDateTime createdDate;

    @NotNull
    private Boolean isEnabled;

    @JsonProperty
    private LocalDateTime disabledDate;
    
    @JsonProperty
    private List<UserTopic> topicList;
}
