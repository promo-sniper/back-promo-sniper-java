package com.anorneto.promosniper.presenters.controller;


import com.anorneto.promosniper.infrastructure.repositories.TelegramRepository;
import com.codahale.metrics.annotation.Timed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/telegram")
@Produces(MediaType.APPLICATION_JSON)
public class TelegramController {


    @GET
    @Path("")
    @Timed
    public Response getUser() throws IOException {
        final TelegramRepository telegramRepository = new TelegramRepository();
        return telegramRepository.getAll();
    }
}
