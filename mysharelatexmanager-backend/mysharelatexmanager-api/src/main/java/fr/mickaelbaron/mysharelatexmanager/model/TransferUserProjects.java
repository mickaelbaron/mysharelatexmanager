package fr.mickaelbaron.mysharelatexmanager.model;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TransferUserProjects {

	@JsonbProperty("current_owner_ref")
	private String currentOwnerId;

	@JsonbProperty("new_owner_ref")
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
