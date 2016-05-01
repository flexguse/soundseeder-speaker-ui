/**
 * 
 */
package de.flexguse.soundseeder.model;

import java.util.Properties;

import lombok.Data;

/**
 * This object contains the software version information taken from the Git
 * information.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
@Data
public class GitRepositoryState {

	private String tags;

	private String commitIdAbbrev;

	private String commitUserEmail;

	private String commitMessageFull;

	private String commitId;

	private String commitIdDescribeShort;

	private String commitMessageShort;

	private String commitUserName;

	private String buildUserName;

	private String commitIdDescribe;

	private String buildUserEmail;

	private String branch;

	private String commitTime;

	private String buildTime;

	private String buildVersion;
	
	private String remoteOriginUrl;

	/**
	 * Constructor which fills the state attributes getting them from the
	 * properties file.
	 * 
	 * @param properties
	 */
	public GitRepositoryState(Properties properties) {

		if (properties != null) {

			this.branch = properties.getProperty("git.branch");
			this.buildTime = properties.getProperty("git.build.time");
			this.buildUserEmail = properties.getProperty("git.build.user.email");
			this.buildUserName = properties.getProperty("git.build.user.name");
			this.commitId = properties.getProperty("git.commit.id");
			this.commitIdAbbrev = properties.getProperty("git.commit.id.abbrev");
			this.commitIdDescribe = properties.getProperty("git.commit.id.describe");
			this.commitIdDescribeShort = properties.getProperty("git.commit.id.describe-short");
			this.commitMessageFull = properties.getProperty("git.commit.message.full");
			this.commitMessageShort = properties.getProperty("git.commit.message.short");
			this.commitTime = properties.getProperty("git.commit.time");
			this.commitUserEmail = properties.getProperty("git.commit.user.email");
			this.commitUserName = properties.getProperty("git.commit.user.name");
			this.remoteOriginUrl = properties.getProperty("git.remote.origin.url");
			this.tags = properties.getProperty("git.tags");
			this.buildVersion = properties.getProperty("git.build.version");

		}

	}

}
