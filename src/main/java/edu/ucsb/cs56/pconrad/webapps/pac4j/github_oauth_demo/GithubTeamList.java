package edu.ucsb.cs56.pconrad.webapps.pac4j.github_oauth_demo;

// Doc is at: http://github-api.kohsuke.org/

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.util.ArrayList;

public class GithubTeamList  {
	
    public java.util.Map<String,GHTeam> teams;
    public GitHub github;
    public GitHubBuilder ghb;
    public GHOrganization org;
    
    public GithubTeamList(String orgname, String oauthToken) throws java.io.IOException {
	this.ghb = new GitHubBuilder().withOAuthToken(oauthToken);
	this.github = ghb.build();
	this.org = github.getOrganization(orgname); 
	this.teams = this.org.getTeams();
    }
}
