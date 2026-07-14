package fr.mickaelbaron.mysharelatexmanager.model;

import java.util.List;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@JsonbNillable(false)
public class UserResult extends AbstractResult {

	private List<User> data;

	@JsonbProperty("update_user")
	private User updatedUser;

	@JsonbProperty("owned_projects")
	private List<Project> ownedProjects;

	@JsonbProperty("collaborators_project")
	private List<Project> collaboratorsProject;

	@JsonbProperty("remove_collaborator_projects_number")
	private Long removedCollaboratorProjectsNumber;

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
		result = prime * result + ((collaboratorsProject == null) ? 0 : collaboratorsProject.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((ownedProjects == null) ? 0 : ownedProjects.hashCode());
		result = prime * result
				+ ((removedCollaboratorProjectsNumber == null) ? 0 : removedCollaboratorProjectsNumber.hashCode());
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
		if (collaboratorsProject == null) {
			if (other.collaboratorsProject != null)
				return false;
		} else if (!collaboratorsProject.equals(other.collaboratorsProject))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (ownedProjects == null) {
			if (other.ownedProjects != null)
				return false;
		} else if (!ownedProjects.equals(other.ownedProjects))
			return false;
		if (removedCollaboratorProjectsNumber == null) {
			if (other.removedCollaboratorProjectsNumber != null)
				return false;
		} else if (!removedCollaboratorProjectsNumber.equals(other.removedCollaboratorProjectsNumber))
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

	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	public List<Project> getCollaboratorsProject() {
		return collaboratorsProject;
	}

	public void setCollaboratorsProject(List<Project> collaboratorsProject) {
		this.collaboratorsProject = collaboratorsProject;
	}

	public Long getRemovedCollaboratorProjectsNumber() {
		return removedCollaboratorProjectsNumber;
	}

	public void setRemovedCollaboratorProjectsNumber(Long removedCollaboratorProjectsNumber) {
		this.removedCollaboratorProjectsNumber = removedCollaboratorProjectsNumber;
	}
}
