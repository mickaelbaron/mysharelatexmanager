package fr.mickaelbaron.mysharelatexmanager.dao;

import java.util.List;
import java.util.Optional;

import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public interface IProjectDAO {

	/**
	 * @param sorted
	 * @return
	 */
	List<ProjectEntity> getAllProjects(List<SortedData> sorted);

	/**
	 * @param filter
	 * @return
	 */
	List<ProjectEntity> getAllProjects(String filter);

	/**
	 * @return
	 */
	List<ProjectEntity> getAllProjects();

	/**
	 * 
	 */
	List<ProjectEntity> getAllProjects(List<SortedData> sorted, String filter);

	/**
	 * @param updateValue
	 * @return
	 */
	boolean updateProject(ProjectEntity updateValue);

	/**
	 * @param oldOwnerId
	 * @param newOwnerId
	 * @return
	 */
	boolean updateProjectsOwner(String oldOwnerId, String newOwnerId);

	/**
	 * @param oldOwnerId
	 * @param newOwnerId
	 * @return
	 */
	List<ProjectEntity> transferAllProjects(String oldOwnerId, String newOwnerId);

	/**
	 * @param oldOwnerId
	 * @param newOwnerId
	 * @return
	 */
	List<ProjectEntity> transferProjectsIfExistingCollaborators(String oldOwnerId, String newOwnerId);

	/**
	 * @param oldOwnerId
	 * @param newOwnerId
	 * @return
	 */
	List<ProjectEntity> transferProjectsIfNewOwnerIsCollaborator(String oldOwnerId, String newOwnerId);

	/**
	 * @param id
	 * @return
	 */
	Optional<ProjectEntity> getProjectById(String id);

	/**
	 * @param ownerId
	 * @return
	 */
	List<ProjectEntity> getAllProjectsByOwnerId(String ownerId);

	/**
	 * @param ownerId
	 * @return
	 */
	List<ProjectEntity> getAllProjectsByCollaboratorsId(String ownerId);
	
	/**
	 * @param collaboratorId
	 */
	Optional<Long> removeCollaborator(String collaboratorId);
}
