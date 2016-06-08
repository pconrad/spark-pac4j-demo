package org.pac4j.demo.spark;

import java.util.HashMap;
import java.util.Map;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;


import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.RequiresAuthenticationFilter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

@SuppressWarnings({"unchecked"})
public class SparkPac4jDemo {


	private final static Logger logger = LoggerFactory.getLogger(SparkPac4jDemo.class);

	private final static MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

	public static void main(String[] args) {
		port(8080);
		final Config config = new DemoConfigFactory(templateEngine).build();

		get("/", SparkPac4jDemo::index, templateEngine);
		final Route callback = new CallbackRoute(config);
		get("/callback", callback);
		post("/callback", callback);
        final RequiresAuthenticationFilter 
			facebookFilter = new RequiresAuthenticationFilter(config, "FacebookClient", "", "excludedPath");
        before("/facebook", facebookFilter);
		before("/facebook/*", facebookFilter);

		get("/facebook", SparkPac4jDemo::protectedIndex, templateEngine);

		exception(Exception.class, (e, request, response) -> {
			logger.error("Unexpected exception", e);
			response.body(templateEngine.render(new ModelAndView(new HashMap<>(), "error500.mustache")));
		});
    }

	private static ModelAndView index(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profile", getUserProfile(request, response));
		return new ModelAndView(map, "index.mustache");
	}

	private static ModelAndView protectedIndex(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profile", getUserProfile(request, response));
		return new ModelAndView(map, "protectedIndex.mustache");
	}

	private static UserProfile getUserProfile(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.get(true);
	}
}
