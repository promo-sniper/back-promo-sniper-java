package com.anorneto.promosniper.presenters.controller;

import com.anorneto.promosniper.domain.dto.TelegramPromoDTO;
import com.anorneto.promosniper.infrastructure.repositories.TelegramRepository;
import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Telegram", description = "Telegram Scrapping Routes")
public class TelegramController {
    private static final List<String> telegramChannels =
            new ArrayList<>(Arrays.asList("BenchPromo", "rapaduraofertas"));
    TelegramRepository telegramRepository;

    // TODO: Do dependency injection here later
    public TelegramController() {
        this.telegramRepository = new TelegramRepository();
    }

    @GET
    @Path("channel")
    @Timed
    public Response getTelegramChannels() {
        return Response.ok().entity(telegramChannels).build();
    }

    @POST
    @Path("channel")
    @Timed
    public Response addTelegramChannel(@NotBlank @QueryParam("channelName") final String channelName) {
        telegramChannels.add(channelName);
        return Response.ok(telegramChannels).build();
    }

    @GET
    @Path("promo")
    @Timed
    public Response getTelegramPromos(@NotBlank @QueryParam("channelName") final String channelName)
            throws IOException {
        List<TelegramPromoDTO> telegramPromoList = telegramRepository.getAll(channelName);
        return Response.ok().entity(telegramPromoList).build();
    }
}
