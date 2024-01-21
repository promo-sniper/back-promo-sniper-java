package com.anorneto.promosniper.presenters.common;

import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class StatusCodeFeature implements DynamicFeature {
    @Override // TODO -> do logic here to get status code from response
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().getAnnotation(StatusCode.class) != null) {
            context.register(StatusCodeFilter.class);
        }
    }
}