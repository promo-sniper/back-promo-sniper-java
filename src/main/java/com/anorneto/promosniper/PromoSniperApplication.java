package com.anorneto.promosniper;

import com.anorneto.promosniper.presenters.common.CORSFilter;
import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.jobs.Job;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.jdbi.v3.guava.GuavaPlugin;
import org.jdbi.v3.jodatime2.JodaTimePlugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

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

        // Guicey Initialization
        GuiceBundle guiceBundle = GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackage().getName())
                .bundles(JdbiBundle.<PromoSniperConfiguration>forDatabase((conf, env) -> conf.getDataSourceFactory())
                        .withPlugins(
                                new SqlObjectPlugin(), new PostgresPlugin(), new GuavaPlugin(), new JodaTimePlugin())
                        .withEagerInitialization())
                .build();
        bootstrap.addBundle(guiceBundle);

        SharedMetricRegistries.add(Job.DROPWIZARD_JOBS_KEY, bootstrap.getMetricRegistry());

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
        // TODO -> Create Bundle for This
        // Jersey Cors Filter
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.setAllowCredentials(true);
        corsFilter.setAllowedHeaders(
                "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Cache-Control");
        corsFilter.setAllowedOrigins(Set.of("*", "http://localhost:3000", "https://localhost:3000"));
        corsFilter.setAllowedMethods("GET,PUT,PATCH,POST,DELETE,OPTIONS");
        corsFilter.setCorsMaxAge(1800);
        environment.jersey().register(corsFilter);

        // Status Filter
        // environment.jersey().register(StatusCodeFilter.class);

        //  HealthChecks
        // AppHealthCheck appHealthCheck = new AppHealthCheck();
        // environment.healthChecks().register("appHealthCheck", appHealthCheck);

    }
}
