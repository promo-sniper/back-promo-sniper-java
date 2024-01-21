package com.anorneto.promosniper;

import com.anorneto.promosniper.domain.usecase.jobs.ScrapTelegramJob;
import com.anorneto.promosniper.presenters.common.StatusCodeFeature;
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
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;

import java.util.EnumSet;
import java.util.List;

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
        // environment.jersey().register(new UserResource(jdbi));
        // Servlets Cors Filter
        FilterRegistration.Dynamic corsFilter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,PATCH,POST,DELETE,OPTIONS,HEAD");
        corsFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilter.setInitParameter(
                CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                "Authorization,X-Requested-With,Content-Type,Content-Length,Accept,Origin,Cache-Control,If-Modified-Since,Pragma");
        corsFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        // DO NOT pass a preflight request to down-stream auth filters
        // unauthenticated preflight requests should be permitted by spec
        corsFilter.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

        // Status Filter
        environment.jersey().register(StatusCodeFeature.class);

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
