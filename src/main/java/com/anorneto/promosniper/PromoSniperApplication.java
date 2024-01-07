package com.anorneto.promosniper;

import com.anorneto.promosniper.presenters.controller.TelegramController;
import com.anorneto.promosniper.presenters.controller.UserController;
import com.anorneto.promosniper.presenters.helpers.AppHealthCheck;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

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
        // TODO: application initialization
        ObjectMapper jackssonObjectMapper = bootstrap.getObjectMapper();
        jackssonObjectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public void run(
            final PromoSniperConfiguration configuration,
            final Environment environment
    ) {
        environment.jersey().register(UserController.class);
        environment.jersey().register(TelegramController.class);
        
        AppHealthCheck appHealthCheck = new AppHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("appHealthCheck", appHealthCheck);
    }

}
