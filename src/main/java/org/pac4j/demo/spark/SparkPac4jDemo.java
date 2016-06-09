package org.pac4j.demo.spark;

// pac4j Javadoc: http://www.pac4j.org/apidocs/pac4j/1.9.0/index.html

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


import org.pac4j.oauth.profile.github.GitHubProfile;

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
			githubFilter = new RequiresAuthenticationFilter(config, "GitHubClient");
        before("/github", githubFilter);
		before("/github/*", githubFilter);

		get("/github", SparkPac4jDemo::githubMV, templateEngine);

		get("/github/repos", SparkPac4jDemo::githubRepos, templateEngine);

		get("/logout", new ApplicationLogoutRoute(config));

		exception(Exception.class, (e, request, response) -> {
			logger.error("Unexpected exception", e);
			response.body(templateEngine.render(new ModelAndView(new HashMap<>(), "error500.mustache")));
		});
    }

	private static ModelAndView index(final Request request, final Response response) {
		logger.info("index...");
		final Map map = new HashMap();
		map.put("profile", getUserProfile(request, response));
		return new ModelAndView(map, "index.mustache");
	}



	private static ModelAndView githubMV(final Request request, final Response response) {
		logger.info("githubMV...");
		final Map map = new HashMap();
		map.put("profile", getUserProfile(request, response));
		return new ModelAndView(map, "github.mustache");
	}



	private static ModelAndView githubRepos(final Request request, final Response response) {
		logger.info("githubRepos...");
		final Map map = new HashMap();
		UserProfile up = getUserProfile(request, response);

		GitHubProfile ghp = (GitHubProfile) up;

		String oauthToken = ghp.getAccessToken();


		GithubRepoList grl = null;
		String message="";
		try {
			grl = new GithubRepoList("UCSB-CS56-Projects",oauthToken);
		} catch (java.io.IOException ioe) {
			message = "Exception: " + ioe;
		}

		

		map.put("repolist",grl != null ? grl.repos : null);
		map.put("profile", up);
		map.put("message", message);
		map.put("org", grl.org.getLogin());
		
		return new ModelAndView(map, "githubRepoList.mustache");
	}


	private static UserProfile getUserProfile(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.get(true);
	}
}
