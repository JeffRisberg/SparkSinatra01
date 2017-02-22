package com.incra.sparkui.template;

/**
 */

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.eclipse.jetty.io.RuntimeIOException;
import spark.ModelAndView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyTemplateEngine extends spark.TemplateEngine {
    private Handlebars handlebars;

    public MyTemplateEngine() {
        this("/templates");
    }

    public MyTemplateEngine(String resourceRoot) {
        ClassPathTemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix(resourceRoot);
        templateLoader.setSuffix((String) null);
        this.handlebars = new Handlebars(templateLoader);
        Cache cache = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).maximumSize(1000L).build();
        this.handlebars = this.handlebars.with(new GuavaTemplateCache(cache));
        this.handlebars.registerHelpers(MyTemplateHelpers.class);
    }

    public String render(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();

        try {
            Template e = this.handlebars.compile(viewName);
            return e.apply(modelAndView.getModel());
        } catch (IOException var4) {
            throw new RuntimeIOException(var4);
        }
    }
}
