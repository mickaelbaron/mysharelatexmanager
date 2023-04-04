package fr.mickaelbaron.mysharelatexmanager.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@JsonInclude(Include.NON_NULL)
public class ProjectResult extends AbstractResult {

	private List<Project> data;

	/**
	 * Used before and after update.
	 */
	private Project updateProject;

	/**
	 * All users to help for choosing owner and collaberators.
	 */
	private List<User> users;

	public List<Project> getData() {
		return data;
	}

	public void setData(List<Project> data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((updateProject == null) ? 0 : updateProject.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProjectResult other = (ProjectResult) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (updateProject == null) {
			if (other.updateProject != null) {
				return false;
			}
		} else if (!updateProject.equals(other.updateProject)) {
			return false;
		}
		if (users == null) {
			if (other.users != null) {
				return false;
			}
		} else if (!users.equals(other.users)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ProjectResult [data=" + data + ", updateProject=" + updateProject + ", users=" + users + "]";
	}

	public Project getUpdateProject() {
		return updateProject;
	}

	public void setUpdateProject(Project updateProject) {
		this.updateProject = updateProject;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
