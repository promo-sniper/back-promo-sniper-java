package com.anorneto.promosniper;

import com.anorneto.promosniper.presenters.controller.TelegramController;
import com.anorneto.promosniper.presenters.controller.UserController;
import com.anorneto.promosniper.presenters.helpers.AppHealthCheck;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class PromoSniperApplication extends Application<PromoSniperConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PromoSniperApplication().run(args);
    }

    @Override
    public String getName() {
        return "PromoSniper";
    }

    @Override
    public void initialize(final Bootstrap<PromoSniperConfiguration> bootstrap) {
        // Jackson Initialization
        ObjectMapper jackssonObjectMapper = bootstrap.getObjectMapper();
        jackssonObjectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        jackssonObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Config Date Format
        jackssonObjectMapper.registerModule(new JavaTimeModule());

        // Swagger Initialization
        bootstrap.addBundle(new SwaggerBundle<>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(PromoSniperConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(final PromoSniperConfiguration configuration, final Environment environment) {
        // Register new routes here
        environment.jersey().register(UserController.class);
        environment.jersey().register(TelegramController.class);

        AppHealthCheck appHealthCheck = new AppHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("appHealthCheck", appHealthCheck);
    }
}
