package com.anorneto.promosniper.presenters.common;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class StatusCodeFilter implements ContainerResponseFilter {

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)
            throws IOException {
        CommonApiResponse<?> commonApiResponse = (CommonApiResponse<?>) containerResponseContext.getEntity();
        int statusCode = commonApiResponse.getStatusCode();
        containerResponseContext.setStatus(statusCode);
    }
}