package com.anorneto.promosniper.presenters.common;

import io.dropwizard.core.setup.Environment;
import io.dropwizard.jobs.Job;
import ru.vyarus.dropwizard.guice.module.installer.FeatureInstaller;
import ru.vyarus.dropwizard.guice.module.installer.install.TypeInstaller;
import ru.vyarus.dropwizard.guice.module.installer.util.FeatureUtils;
import ru.vyarus.dropwizard.guice.module.installer.util.Reporter;

public class JobsInstaller implements FeatureInstaller, TypeInstaller<Job> {

    private final Reporter reporter = new Reporter(JobsInstaller.class, "jobs =");

    @Override
    public boolean matches(Class<?> type) {
        return FeatureUtils.is(type, Job.class);
    }

    @Override
    public void install(Environment environment, Class<Job> type) {
        // here we can also look for class annotations and show more info in console
        // (omitted for simplicity)
        reporter.line("(%s)", type.getName());
    }

    @Override
    public void report() {
        reporter.report();
    }
}