package fr.mickaelbaron.mysharelatexmanager.cfg;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public interface IConfigExecution {

	String getMongoDBUrl();

	String getIdentificationUser();

	String getIdentificationPassword();

	String getEncryptPassword();

	String getEncryptNoise();

	String getSessionTimeout();

	String displayAllConfigExecution();
	
	String getUserFilesPath();
}
