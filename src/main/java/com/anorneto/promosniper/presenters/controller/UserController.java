package com.anorneto.promosniper.presenters.controller;

import com.anorneto.promosniper.domain.dto.UserDTO;
import com.codahale.metrics.annotation.Timed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @GET
    @Path("/{userId}")
    @Timed
    public UserDTO getUser(@PathParam("userId") int userId) {
        final UserDTO user = UserDTO.builder().id(userId).build();
        return user;
    }
}
