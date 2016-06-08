package org.pac4j.demo.spark;

import org.pac4j.core.authorization.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.oauth.client.GitHubClient;
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

		// final TwitterClient twitterClient = new TwitterClient("CoxUiYwQOSFDReZYdjigBA",
        //                                                       "2kAzunH5Btc4gRSaMr7D7MkyoJ5u1VzbOOzE8rBofs");

		final GitHubClient githubClient = new GitHubClient("051b232208b0a299ab03",
                                                               "f8e8d24744d8c56f1285c60274b6861aab862303");

        final Clients clients = new Clients("http://localhost:8080/callback" 
											,facebookClient 
											,githubClient
											/* ,twitterClient */ );

        final Config config = new Config(clients);
        config.setHttpActionAdapter(new DemoHttpActionAdapter(templateEngine)); 
        return config;
    }
}
