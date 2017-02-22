package com.incra.sparkui.binding;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.incra.sparkui.config.AppConfig;
import com.incra.sparkui.config.ArchaiusAppConfig;
import com.incra.sparkui.template.MyTemplateEngine;
import spark.TemplateEngine;

/**
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {

        // app config
        bind(AppConfig.class).to(ArchaiusAppConfig.class).in(Scopes.SINGLETON);

        bind(TemplateEngine.class).to(MyTemplateEngine.class);
    }
}
