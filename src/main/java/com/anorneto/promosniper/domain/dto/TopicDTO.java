package com.anorneto.promosniper.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TopicDTO {
    @JsonProperty
    private int id;

    @NotEmpty
    @JsonProperty
    private List<String> keywordList;

    @JsonProperty
    private List<PromoDTO> promoList;
}
