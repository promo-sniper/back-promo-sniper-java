package com.anorneto.promosniper.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
public class TelegramPromoDTO {
    @NotNull
    private final int numVisualizations;

    @NotBlank
    private final String text;

    private final String photoUrl;

    @NotBlank
    private final LocalDateTime dateTime;


    public TelegramPromoDTO(String numVisualizations, String text, String photoUrl, String dateTime) {
        this.numVisualizations = Integer.parseInt(numVisualizations);
        this.text = text;
        Pattern urlRegexPattern = Pattern.compile(".*background-image:url\\('(.*)'\\)");
        this.photoUrl = urlRegexPattern.matcher(photoUrl).group(1);
        this.dateTime = LocalDateTime.parse(dateTime);
    }
}
