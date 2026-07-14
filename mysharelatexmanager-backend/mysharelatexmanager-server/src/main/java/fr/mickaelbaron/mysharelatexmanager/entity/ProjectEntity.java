package fr.mickaelbaron.mysharelatexmanager.entity;

import java.util.Date;
import java.util.List;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ProjectEntity {

	private String id;

	private String ownerId;

	private String owner;

	private String name;

	private String description;

	private Boolean isActive;

	private List<String> collaboratorsId;

	private List<ShortUserEntity> collaborators;
	
	private Date lastUpdated;

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonbProperty("owner_ref")
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String owner) {
		this.ownerId = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonbProperty("active")
	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@JsonbProperty("collaborator_refs")
	public List<String> getCollaboratorsId() {
		return collaboratorsId;
	}

	public void setCollaboratorsId(List<String> collaboratorsId) {
		this.collaboratorsId = collaboratorsId;
	}

	@Override
	public String toString() {
		return "ProjectEntity [id=" + id + ", ownerId=" + ownerId + ", owner=" + owner + ", name=" + name
				+ ", description=" + description + ", isActive=" + isActive
				+ ", collaboratorsId=" + collaboratorsId + ", collaborators=" + collaborators + ", lastUpdated="
				+ lastUpdated + "]";
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collaboratorsId == null) ? 0 : collaboratorsId.hashCode());
		result = prime * result + ((collaborators == null) ? 0 : collaborators.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectEntity other = (ProjectEntity) obj;
		if (collaboratorsId == null) {
			if (other.collaboratorsId != null)
				return false;
		} else if (!collaboratorsId.equals(other.collaboratorsId))
			return false;
		if (collaborators == null) {
			if (other.collaborators != null)
				return false;
		} else if (!collaborators.equals(other.collaborators))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		return true;
	}

	public List<ShortUserEntity> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<ShortUserEntity> collaborators) {
		this.collaborators = collaborators;
	}
}
