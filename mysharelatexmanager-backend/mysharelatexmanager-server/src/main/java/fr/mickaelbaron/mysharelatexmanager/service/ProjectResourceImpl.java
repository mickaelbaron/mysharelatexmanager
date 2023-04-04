package fr.mickaelbaron.mysharelatexmanager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant;
import fr.mickaelbaron.mysharelatexmanager.api.ProjectResource;
import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.EntityFactory;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.ProjectResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ProjectResourceImpl implements ProjectResource {

	@Inject
	IProjectDAO refProjectDAO;

	@Inject
	IUserDAO refUserDAO;

	@Override
	public ProjectResult getAllProjects(String sorted, String filter) {
		List<SortedData> sorts = new ArrayList<>();

		if (sorted != null && !sorted.isEmpty()) {
			final List<String> split = Arrays.asList(sorted.split(","));

			for (String string : split) {
				final String[] currentSortsData = string.split("\\|");
				if (currentSortsData != null && currentSortsData.length == 2 && currentSortsData[0] != null
						&& currentSortsData[1] != null) {
					SortedData newSortedData = new SortedData();
					newSortedData.setName(currentSortsData[0]);
					newSortedData.setAscendant(currentSortsData[1].equals("asc"));
					sorts.add(newSortedData);
				}
			}
		}

		if (filter == null) {
			filter = "";
		}

		final List<Project> allProjects = EntityFactory.createProjects(refProjectDAO.getAllProjects(sorts, filter));

		if (null == allProjects) {
			throw new WebApplicationException("Something is wrong.", Status.INTERNAL_SERVER_ERROR);
		} else {
			ProjectResult root = new ProjectResult();
			root.setTotal(allProjects.size());
			root.setData(allProjects);
			return root;
		}
	}

	@Override
	public Response updateProject(Project current) {
		if (null == current) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		Optional<ProjectEntity> projectById = this.refProjectDAO.getProjectById(current.getId());

		if (projectById.isPresent()) {
			ProjectEntity createProjectEntity = EntityFactory.createProjectEntity(current);
			if (this.refProjectDAO.updateProject(createProjectEntity)) {
				return Response.ok().build();
			} else {
				return Response.noContent().build();
			}
		} else {
			throw new WebApplicationException(MySharelatexManagerConstant.PROJECT_NOT_FOUND_MSG, Status.NOT_FOUND);
		}
	}

	@Override
	public ProjectResult prepareUpdateProject(String id) {
		if (null == id) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		// Retrieve Project from its id.
		final Optional<ProjectEntity> projectById = this.refProjectDAO.getProjectById(id);

		if (projectById.isPresent()) {
			ProjectResult newProjectResult = new ProjectResult();
			// Retrieve all Users.
			newProjectResult.setUsers(EntityFactory.createUsers(refUserDAO.getAllUsers()));
			newProjectResult.setUpdateProject(EntityFactory.createProject(projectById.get()));
			return newProjectResult;
		} else {
			throw new WebApplicationException(MySharelatexManagerConstant.PROJECT_NOT_FOUND_MSG, Status.NO_CONTENT);
		}
	}

	@Override
	public ProjectResult getProjectsByOwner(String id) {
		if (null == id) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		// Retrieve projects from its owner id.
		final List<ProjectEntity> allProjectsByOwnerId = this.refProjectDAO.getAllProjectsByOwnerId(id);

		ProjectResult newProjectResult = new ProjectResult();
		if (allProjectsByOwnerId != null && !allProjectsByOwnerId.isEmpty()) {
			newProjectResult.setData(EntityFactory.createProjects(allProjectsByOwnerId));
		} else {
			newProjectResult.setData(new ArrayList<Project>());
		}

		return newProjectResult;
	}
}
