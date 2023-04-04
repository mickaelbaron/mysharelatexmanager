package fr.mickaelbaron.mysharelatexmanager.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class UserEntity {

	private String id;

	private String lastName;

	private boolean isAdmin;

	private String institution;

	private String firstName;

	private String email;

	private String hashedPassword;

	private Integer loginCount;

	private Date lastLoggedIn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", lastName=" + lastName + ", isAdmin=" + isAdmin + ", institution=" + institution
				+ ", firstName=" + firstName + ", email=" + email + ", hashedPassword=" + hashedPassword
				+ ", loginCount=" + loginCount + ", lastLoggedIn=" + lastLoggedIn + "]";
	}

	@JsonProperty("isAdmin")
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((hashedPassword == null) ? 0 : hashedPassword.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((institution == null) ? 0 : institution.hashCode());
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + ((lastLoggedIn == null) ? 0 : lastLoggedIn.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((loginCount == null) ? 0 : loginCount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (hashedPassword == null) {
			if (other.hashedPassword != null) {
				return false;
			}
		} else if (!hashedPassword.equals(other.hashedPassword)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (institution == null) {
			if (other.institution != null) {
				return false;
			}
		} else if (!institution.equals(other.institution)) {
			return false;
		}
		if (isAdmin != other.isAdmin) {
			return false;
		}
		if (lastLoggedIn == null) {
			if (other.lastLoggedIn != null) {
				return false;
			}
		} else if (!lastLoggedIn.equals(other.lastLoggedIn)) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (loginCount == null) {
			if (other.loginCount != null) {
				return false;
			}
		} else if (!loginCount.equals(other.loginCount)) {
			return false;
		}
		return true;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
}
