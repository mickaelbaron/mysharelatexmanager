package fr.mickaelbaron.mysharelatexmanager.model;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ToolsResult {

	private String message;

	@JsonbProperty("error")
	private boolean isError;

	public ToolsResult() {
	}

	public ToolsResult(boolean pIsError, String pMessage) {
		this.isError = pIsError;
		this.message = pMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}
}
