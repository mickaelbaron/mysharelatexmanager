package fr.mickaelbaron.mysharelatexmanager.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@JsonInclude(Include.NON_NULL)
public class UserResult extends AbstractResult {

	private List<User> data;

	private User updatedUser;

	private List<Project> ownerProject;

	private List<Project> collaberatorsProject;

	private Long removedCollaberatorProjectsNumber;

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> users) {
		this.data = users;
	}

	@Override
	public String toString() {
		return "UserResult [data=" + data + ", updatedUser=" + updatedUser + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((collaberatorsProject == null) ? 0 : collaberatorsProject.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((ownerProject == null) ? 0 : ownerProject.hashCode());
		result = prime * result
				+ ((removedCollaberatorProjectsNumber == null) ? 0 : removedCollaberatorProjectsNumber.hashCode());
		result = prime * result + ((updatedUser == null) ? 0 : updatedUser.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserResult other = (UserResult) obj;
		if (collaberatorsProject == null) {
			if (other.collaberatorsProject != null)
				return false;
		} else if (!collaberatorsProject.equals(other.collaberatorsProject))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (ownerProject == null) {
			if (other.ownerProject != null)
				return false;
		} else if (!ownerProject.equals(other.ownerProject))
			return false;
		if (removedCollaberatorProjectsNumber == null) {
			if (other.removedCollaberatorProjectsNumber != null)
				return false;
		} else if (!removedCollaberatorProjectsNumber.equals(other.removedCollaberatorProjectsNumber))
			return false;
		if (updatedUser == null) {
			if (other.updatedUser != null)
				return false;
		} else if (!updatedUser.equals(other.updatedUser))
			return false;
		return true;
	}

	public User getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(User updatedUser) {
		this.updatedUser = updatedUser;
	}

	public List<Project> getOwnerProject() {
		return ownerProject;
	}

	public void setOwnerProject(List<Project> ownerProject) {
		this.ownerProject = ownerProject;
	}

	public List<Project> getCollaberatorsProject() {
		return collaberatorsProject;
	}

	public void setCollaberatorsProject(List<Project> collaberatorsProject) {
		this.collaberatorsProject = collaberatorsProject;
	}

	public Long getRemovedCollaberatorProjectsNumber() {
		return removedCollaberatorProjectsNumber;
	}

	public void setRemovedCollaberatorProjectsNumber(Long removedCollaberatorProjectsNumber) {
		this.removedCollaberatorProjectsNumber = removedCollaberatorProjectsNumber;
	}
}
