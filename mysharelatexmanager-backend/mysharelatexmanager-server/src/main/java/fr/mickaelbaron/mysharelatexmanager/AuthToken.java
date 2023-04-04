package fr.mickaelbaron.mysharelatexmanager;

import java.util.Date;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AuthToken {

	protected String identifiant;

	protected Date startDate;

	public AuthToken(String pIdentifiant, Date pStartDate) {
		this.identifiant = pIdentifiant;
		this.startDate = pStartDate;
	}

	public boolean isValid() {
		return identifiant != null && startDate != null;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public String getStartDateValue() {
		return Long.toString(startDate.getTime());
	}

	public Date getStartDate() {
		return startDate;
	}

	@Override
	public String toString() {
		return "AuthToken [identifiant=" + identifiant + ", startDate=" + startDate + "]";
	}
}