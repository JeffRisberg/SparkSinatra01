package com.incra.sparkui;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.incra.sparkui.binding.AppModule;
import com.incra.sparkui.config.AppConfig;
import com.incra.sparkui.controllers.BasicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

import static com.incra.sparkui.controllers.ApiUtils.toApiResponse;
import static com.incra.sparkui.utils.Standard.getInt;
import static spark.Spark.*;

/**
 * @author Jeff Risberg
 * @since 10/12/15
 */
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    private AppConfig appConfig;

    private BasicController basic;

    @Inject
    public Application(AppConfig appConfig, BasicController basic) {

        this.appConfig = appConfig;
        this.basic = basic;
    }

    private String version(Request req, Response res) {
        return toApiResponse(appConfig.getString("qm.build.version", "UNKNOWN"));
    }

    private String shutdown(Request req, Response res) {
        logger.info("Received shutdown command, starting shutdown now!");
        System.exit(0);
        return toApiResponse("Bye Bye!");
    }

    public static void main(String[] args) {

        //ConfigInitializer.getInstance().init();
        Injector injector = Guice.createInjector(new AppModule());
        //injector.getInstance(JdbcConfigInitializer.class).installJdbcSource();

        TemplateEngine templateEngine = injector.getInstance(TemplateEngine.class);
        Application app = injector.getInstance(Application.class);

        // bind to port in PORT system env (or 4567 by default)
        //port(getInt(System.getenv("PORT"), 4567));

        // Application
        staticFileLocation("/www");
        get("/version", app::version);
        get("/shutdown", app::shutdown);

        // Basic
        get("/basic", app.basic::index, templateEngine);

        System.out.println("Ready");
    }
}
