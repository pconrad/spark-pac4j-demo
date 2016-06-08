package org.pac4j.demo.spark;

import org.pac4j.core.authorization.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import spark.TemplateEngine;

public class DemoConfigFactory implements ConfigFactory {

    private final TemplateEngine templateEngine;

    public DemoConfigFactory(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Config build() {

        final FacebookClient facebookClient = new FacebookClient("145278422258960", 
																 "be21409ba8f39b5dae2a7de525484da8");

		//        final TwitterClient twitterClient = new TwitterClient("CoxUiYwQOSFDReZYdjigBA",
        //         "2kAzunH5Btc4gRSaMr7D7MkyoJ5u1VzbOOzE8rBofs");


        final Clients clients = new Clients("http://localhost:8080/callback", 
											facebookClient /* ,twitterClient */
											);

        final Config config = new Config(clients); // keep this

        // config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
        config.addMatcher("excludedPath", new ExcludedPathMatcher("^/facebook/notprotected$")); // keep this
        config.setHttpActionAdapter(new DemoHttpActionAdapter(templateEngine)); // keep this
        return config;
    }
}
