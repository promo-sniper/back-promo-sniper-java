package com.anorneto.promosniper;

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
    }

    @Override
    public void run(final PromoSniperConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
