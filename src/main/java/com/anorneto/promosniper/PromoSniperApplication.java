package com.anorneto.promosniper;

import com.anorneto.promosniper.domain.usecase.jobs.ScrapTelegramJob;
import com.anorneto.promosniper.presenters.common.CORSFilter;
import com.anorneto.promosniper.presenters.common.StatusCodeFilter;
import com.anorneto.promosniper.presenters.controller.TelegramController;
import com.anorneto.promosniper.presenters.controller.UserController;
import com.anorneto.promosniper.presenters.healthcheck.AppHealthCheck;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.jobs.JobsBundle;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Set;

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
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor()));

        // Jackson Initialization
        ObjectMapper jackssonObjectMapper = bootstrap.getObjectMapper();
        jackssonObjectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // Config Date Format
        jackssonObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jackssonObjectMapper.registerModule(new JavaTimeModule());

        // Swagger Initialization
        bootstrap.addBundle(new SwaggerBundle<>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(PromoSniperConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
        // Jdbi Exceptions Bundle
        bootstrap.addBundle(new JdbiExceptionsBundle());
    }

    @Override
    public void run(final PromoSniperConfiguration configuration, final Environment environment) {
        // Database Connection
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        // TODO -> Create Bundle for This
        // Jersey Cors Filter
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.setAllowCredentials(true);
        corsFilter.setAllowedHeaders("Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        corsFilter.setAllowedOrigins(Set.of("*"));
        corsFilter.setAllowedMethods("GET,PUT,PATCH,POST,DELETE,OPTIONS");
        corsFilter.setCorsMaxAge(1800);
        environment.jersey().register(corsFilter);

        // Status Filter
        environment.jersey().register(StatusCodeFilter.class);

        // Register new routes here
        environment.jersey().register(UserController.class);
        environment.jersey().register(new TelegramController(jdbi));
        //  HealthChecks
        AppHealthCheck appHealthCheck = new AppHealthCheck();
        environment.healthChecks().register("appHealthCheck", appHealthCheck);

        // Quartz Jobs
        ScrapTelegramJob scrapTelegramJob = new ScrapTelegramJob(jdbi);
        JobsBundle jobsBundle = new JobsBundle(List.of(scrapTelegramJob));
        try {
            if (configuration.shouldRunJobs) {
                jobsBundle.run(configuration, environment);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
