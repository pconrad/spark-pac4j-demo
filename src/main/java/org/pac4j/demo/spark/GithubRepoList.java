package org.pac4j.demo.spark;

// Doc is at: http://github-api.kohsuke.org/

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.util.ArrayList;

public class GithubRepoList  {
	
	public java.util.ArrayList<GHRepository> repos;
	public GitHub github;
	public GitHubBuilder ghb;
	public GHOrganization org;

	public GithubRepoList(String orgname, String oauthToken) throws java.io.IOException {
		this.ghb = new GitHubBuilder().withOAuthToken(oauthToken);
		this.github = ghb.build();
		this.org = github.getOrganization(orgname); 
		this.repos = new java.util.ArrayList<GHRepository>();

		PagedIterable<GHRepository> pi = org.listRepositories(25);

		PagedIterator<GHRepository> it = pi.iterator();
		while (it.hasNext()) {
			GHRepository r = it.next();
			this.repos.add(r);
		}
	}


}