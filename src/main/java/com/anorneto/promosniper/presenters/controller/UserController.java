package com.anorneto.promosniper.presenters.controller;


import com.anorneto.promosniper.domain.dto.UserDTO;
import com.codahale.metrics.annotation.Timed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.atomic.AtomicLong;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public UserController(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Path("/{userId}")
    @Timed
    public UserDTO getUser(@PathParam("userId") int userId) {
        final UserDTO user = UserDTO.builder().id(userId).build();
        return user;
    }
}
