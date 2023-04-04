package fr.mickaelbaron.mysharelatexmanager.model;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TransfertUserProjects {

	private String currentOwnerId;

	private String newOwnerId;

	private MODE_ENUM mode;

	public enum MODE_ENUM {
		NORMAL, FORCED, ALL
	}

	public String getCurrentOwnerId() {
		return currentOwnerId;
	}

	public void setCurrentOwnerId(String currentOwnerId) {
		this.currentOwnerId = currentOwnerId;
	}

	public String getNewOwnerId() {
		return newOwnerId;
	}

	public void setNewOwnerId(String newOwnerId) {
		this.newOwnerId = newOwnerId;
	}

	public MODE_ENUM getMode() {
		return mode;
	}

	public void setMode(MODE_ENUM mode) {
		this.mode = mode;
	}
}
