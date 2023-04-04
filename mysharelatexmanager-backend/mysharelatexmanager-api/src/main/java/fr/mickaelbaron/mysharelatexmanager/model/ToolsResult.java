package fr.mickaelbaron.mysharelatexmanager.model;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ToolsResult {

	private String message;

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
