package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoDTO {
    @JsonProperty
    private int id;

    @NotBlank
    @JsonProperty
    private String sourceType;

    @NotBlank
    @JsonProperty
    private String sourceName;

    @JsonProperty
    private int sourceIdentifier;

    @NotBlank
    @JsonProperty
    private String description;

    @org.hibernate.validator.constraints.URL
    @Null
    @JsonProperty
    private String productUrl;

    @JsonProperty
    private String productName;

    @JsonProperty
    private double productPrice;

    @JsonProperty
    private String productPhoto;

    @NotNull
    @JsonProperty
    private ZonedDateTime createdDate;
}
