package com.anorneto.promosniper.presenters.common;

import com.anorneto.promosniper.PromoSniperConfiguration;
import com.google.inject.Injector;
import io.dropwizard.jobs.GuiceJobManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class QuartzJobManager extends GuiceJobManager {

    @Inject
    public QuartzJobManager(Injector injector, PromoSniperConfiguration configuration) {
        super(configuration, injector);
    }
}