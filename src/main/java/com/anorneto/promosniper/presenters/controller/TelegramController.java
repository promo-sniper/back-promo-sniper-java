package com.anorneto.promosniper.presenters.controller;

import com.anorneto.promosniper.domain.dto.PromoDTO;
import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import com.anorneto.promosniper.infrastructure.repositories.PromoRepository;
import com.anorneto.promosniper.infrastructure.repositories.TelegramRepository;
import com.anorneto.promosniper.presenters.common.CommonApiResponse;
import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Anor Neto
 */
@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Telegram", description = "Telegram Scrapping Routes")
public class TelegramController {
    private static final List<String> telegramChannels =
            new ArrayList<>(Arrays.asList("BenchPromos", "rapaduraofertas"));
    final PromoRepository promoRepository;

    final TelegramRepository telegramRepository;

    @Inject
    public TelegramController(final PromoRepository promoRepository, final TelegramRepository telegramRepository) {
        this.promoRepository = promoRepository;
        this.telegramRepository = telegramRepository;
    }

    @GET
    @Path("channel")
    @Timed
    @Operation(
            summary = "Get Telegram Channels",
            description = "Get Telegram Registered Promos Channels",
            operationId = "getTelegramChannels",
            responses = {@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)})
    public CommonApiResponse<List<String>> getTelegramChannels() {
        CommonApiResponse<List<String>> response = new CommonApiResponse<>();
        return response.ok(telegramChannels);
    }

    @POST
    @Path("channel")
    @Timed
    @Operation(
            summary = "Add Telegram Channel",
            description = "Add Telegram Channel to list of registered channels",
            operationId = "addTelegramChannel",
            responses = {@ApiResponse(responseCode = "201", description = "Created ok", useReturnTypeSchema = true)})
    public CommonApiResponse<List<String>> addTelegramChannel(
            @NotBlank @QueryParam("channelName") final String channelName) {
        CommonApiResponse<List<String>> response = new CommonApiResponse<>();
        telegramChannels.add(channelName);
        return response.created(telegramChannels);
    }

    @GET
    @Path("promo")
    @Timed
    @Operation(
            summary = "Get Telegram Promos",
            description = "Get Telegram Promos from a specific channel",
            operationId = "getTelegramPromos",
            responses = {@ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true)})
    public CommonApiResponse<List<TelegramPromoDTO>> getTelegramPromos(
            @NotBlank @QueryParam("channelName") final String channelName) throws IOException {
        CommonApiResponse<List<TelegramPromoDTO>> response = new CommonApiResponse<>();

        HashMap<String, Integer> maxIdentifierBySourceName = promoRepository.getMaxIdentifierBySourceName("Telegram");
        Integer afterId = maxIdentifierBySourceName.getOrDefault(channelName, null);
        List<TelegramPromoDTO> telegramPromoList = telegramRepository.getAll(channelName, afterId);

        List<PromoDTO> promoDTOList =
                telegramPromoList.stream().map(TelegramPromoDTO::toPromoDTO).toList();

        promoRepository.batchInsert(promoDTOList);
        return response.ok(telegramPromoList);
    }
}
