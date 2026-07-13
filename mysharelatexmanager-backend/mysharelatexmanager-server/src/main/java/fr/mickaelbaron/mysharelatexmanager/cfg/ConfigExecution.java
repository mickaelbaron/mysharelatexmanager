package fr.mickaelbaron.mysharelatexmanager.cfg;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ApplicationScoped
public class ConfigExecution implements IConfigExecution {

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.mongodb.url", defaultValue = "mongodb://localhost:27017")
	private String mongoDBUrl;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.identification.user", defaultValue = "admin")
	private String identificationUser;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.identification.password", defaultValue = "adminadmin")
	private String identificationPassword;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.encrypt.password", defaultValue = "!thisismypassword!")
	private String encryptPassword;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.encrypt.noise", defaultValue = "@BCDEFGHIJKLMNPQRSTUVWXZ&bcdefghijklmnopqrstuvwxyz0123456789")
	private String encryptNoise;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.session.timeout", defaultValue = "60")
	private String sessionTimeout;

	@Inject
	@ConfigProperty(name = "mysharelatexmanager.userfiles.path", defaultValue = "/data/sharelatex/data/user_files/")
	private String userFilesPath;

	@PostConstruct
	public void init() {
		System.out.println("ConfigExecution initialized with: " + this.displayAllConfigExecution());
	}

	public String getUserFilesPath() {
		return userFilesPath;
	}

	public String getMongoDBUrl() {
		return mongoDBUrl;
	}

	public String getIdentificationUser() {
		return identificationUser;
	}

	public String getIdentificationPassword() {
		return identificationPassword;
	}

	public String getEncryptPassword() {
		return encryptPassword;
	}

	public String getEncryptNoise() {
		return encryptNoise;
	}

	public String getSessionTimeout() {
		return sessionTimeout;
	}

	public String toString() {
		return this.displayAllConfigExecution();
	}

	@Override
	public String displayAllConfigExecution() {
		return "ConfigExecution [mysharelatexmanager.mongodb.url=" + mongoDBUrl
				+ ", mysharelatexmanager.identification.user=" + identificationUser
				+ ", mysharelatexmanager.identification.password=" + identificationPassword
				+ ", mysharelatexmanager.encrypt.password=" + encryptPassword + ", mysharelatexmanager.encrypt.noise="
				+ encryptNoise + ", mysharelatexmanager.session.timeout=" + sessionTimeout
				+ ", mysharelatexmanager.userfiles.path=" + userFilesPath + "]";
	}
}
