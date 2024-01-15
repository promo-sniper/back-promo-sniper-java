package com.anorneto.promosniper.domain.dto;

import com.anorneto.promosniper.common.util.NumberParser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class TelegramPromoDTO {
    @NotNull
    private final int numVisualizations;

    @NotBlank
    private final String text;

    private final String photoUrl;

    @NotBlank
    private final ZonedDateTime dateTime;

    private final int telegramId;

    public TelegramPromoDTO(String numVisualizations, String text, String photoUrl, String dateTime, int telegramId) {
        this.text = text;
        this.telegramId = telegramId;

        this.numVisualizations = NumberParser.parseNumber(numVisualizations);

        Pattern photoUrlRegexPattern = Pattern.compile("background-image:url\\('([^']+)'\\)");
        Matcher photoUrlRegexMatcher = photoUrlRegexPattern.matcher(photoUrl);
        this.photoUrl = photoUrlRegexMatcher.find() ? photoUrlRegexMatcher.group(1) : photoUrl;

        this.dateTime = ZonedDateTime.parse(dateTime);
    }
}
