package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserTopic {

    @NotNull
    @JsonProperty
    private int topicId;

    @NotNull
    @JsonProperty
    private LocalDateTime createdDate;

    @NotNull
    private Boolean isEnabled;

    private LocalDateTime disabledDate;
}
