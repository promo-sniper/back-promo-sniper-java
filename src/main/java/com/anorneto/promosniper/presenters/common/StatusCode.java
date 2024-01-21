package com.anorneto.promosniper.presenters.common;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusCode {
    int CREATED = 201;
    int NO_CONTENT = 204;

    int ACCEPTED = 202;

    int value();
}