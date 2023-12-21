package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PromoDTO {
    @JsonProperty
    private int id;

    @NotBlank
    @JsonProperty
    private String source;

    @JsonProperty
    private String description;

    @org.hibernate.validator.constraints.URL
    @NotBlank
    @JsonProperty
    private URL url;

    @JsonProperty
    private String productName;

    @JsonProperty
    private float price;

    @NotNull
    @JsonProperty
    private LocalDateTime createdDate;
}
