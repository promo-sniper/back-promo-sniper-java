package com.anorneto.promosniper.presenters.common;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.annotation.Annotation;

@Provider
public class StatusCodeFilter implements ContainerResponseFilter {

    @Override
    public void filter(
            ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)
            throws IOException {
        if (containerResponseContext.getStatus() == 200) {
            for (Annotation annotation : containerResponseContext.getEntityAnnotations()) {
                if (annotation instanceof StatusCode) {
                    containerResponseContext.setStatus(((StatusCode) annotation).value());
                    break;
                }
            }
        }
    }
}