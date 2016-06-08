package org.pac4j.demo.spark;

// import org.pac4j.cas.client.CasClient;
import org.pac4j.core.authorization.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
// import org.pac4j.http.client.indirect.FormClient;
// import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
// import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
// import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oauth.client.FacebookClient;
// import org.pac4j.oauth.client.TwitterClient;
// import org.pac4j.oidc.client.OidcClient;
// import org.pac4j.saml.client.SAML2Client;
// import org.pac4j.saml.client.SAML2ClientConfiguration;
import spark.TemplateEngine;

public class DemoConfigFactory implements ConfigFactory {

    private final String salt;

    private final TemplateEngine templateEngine;

    public DemoConfigFactory(final String salt, final TemplateEngine templateEngine) {
        this.salt = salt;
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

        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ROLE_ADMIN"));
        config.addMatcher("excludedPath", new ExcludedPathMatcher("^/facebook/notprotected$")); // keep this
        config.setHttpActionAdapter(new DemoHttpActionAdapter(templateEngine)); // keep this
        return config;
    }
}
