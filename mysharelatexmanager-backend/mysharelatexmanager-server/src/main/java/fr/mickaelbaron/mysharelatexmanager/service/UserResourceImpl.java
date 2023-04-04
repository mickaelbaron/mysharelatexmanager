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
import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerUtil;
import fr.mickaelbaron.mysharelatexmanager.api.UserResource;
import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.EntityFactory;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;
import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.TransfertUserProjects;
import fr.mickaelbaron.mysharelatexmanager.model.User;
import fr.mickaelbaron.mysharelatexmanager.model.UserResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class UserResourceImpl implements UserResource {

	@Inject
	IUserDAO refUserDAO;

	@Inject
	IProjectDAO refProjectDAO;

	@Override
	public UserResult getAllUsers(String sorted, String filter) {
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

		final List<UserEntity> allUsers = refUserDAO.getAllUsers(sorts, filter);

		if (null == allUsers) {
			throw new WebApplicationException("Something is wrong.", Status.INTERNAL_SERVER_ERROR);
		} else {
			UserResult root = new UserResult();
			root.setTotal(allUsers.size());
			root.setData(EntityFactory.createUsers(allUsers));
			return root;
		}
	}

	@Override
	public Response updateUser(User current) {
		if (null == current) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		Optional<UserEntity> userEntityById = this.refUserDAO.getUserById(current.getId());

		if (userEntityById.isPresent()) {
			if (current.getHashedPassword() != null && !current.getHashedPassword().isEmpty()) {
				current.setHashedPassword(MySharelatexManagerUtil.hashPassword(current.getHashedPassword()));
			}

			if (this.refUserDAO.updateUser(EntityFactory.createUserEntity(current))) {
				return Response.ok().build();
			} else {
				return Response.noContent().build();
			}
		} else {
			throw new WebApplicationException(MySharelatexManagerConstant.USER_NOT_FOUND_MSG, Status.NOT_FOUND);
		}
	}

	@Override
	public UserResult deleteUser(String id) {
		if (null == id) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		final Optional<UserEntity> userById = this.refUserDAO.getUserById(id);
		if (!userById.isPresent()) {
			throw new WebApplicationException("User not found.", Status.NOT_FOUND);
		}

		// Prepare result.
		UserResult newResult = new UserResult();

		final List<Project> allProjectsByOwnerId = EntityFactory
				.createProjects(this.refProjectDAO.getAllProjectsByOwnerId(id));
		newResult.setOwnerProject(allProjectsByOwnerId);

		final List<Project> allProjectsByCollaberatorsId = EntityFactory
				.createProjects(this.refProjectDAO.getAllProjectsByCollaberatorsId(id));
		newResult.setCollaberatorsProject(allProjectsByCollaberatorsId);

		if (allProjectsByCollaberatorsId.isEmpty() && allProjectsByOwnerId.isEmpty()) {
			final boolean deleteUser = this.refUserDAO.deleteUser(id);

			if (deleteUser) {
				return newResult;
			} else {
				throw new WebApplicationException("Cannot delete user: " + id, Status.NOT_FOUND);
			}
		} else {
			newResult.setOwnerProject(allProjectsByOwnerId);
			newResult.setCollaberatorsProject(allProjectsByCollaberatorsId);
			return newResult;
		}
	}

	@Override
	public UserResult transfertUserProjects(TransfertUserProjects transfertUserProjects) {
		if (null == transfertUserProjects) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		if (transfertUserProjects.getMode() == null) {
			throw new WebApplicationException("Parameter is missing: mode", Status.BAD_REQUEST);
		}

		if (transfertUserProjects.getCurrentOwnerId() != null && !transfertUserProjects.getCurrentOwnerId().isEmpty()) {
			final Optional<UserEntity> userById = this.refUserDAO
					.getUserById(transfertUserProjects.getCurrentOwnerId());

			if (!userById.isPresent()) {
				throw new WebApplicationException(
						"User not found: currentOwnerId (" + transfertUserProjects.getCurrentOwnerId() + ")",
						Status.NOT_FOUND);
			}
		} else {
			throw new WebApplicationException("Parameter is missing: currentOwnerId", Status.BAD_REQUEST);
		}

		if (transfertUserProjects.getNewOwnerId() != null && !transfertUserProjects.getNewOwnerId().isEmpty()) {
			final Optional<UserEntity> userById = this.refUserDAO.getUserById(transfertUserProjects.getNewOwnerId());

			if (!userById.isPresent()) {
				throw new WebApplicationException(
						"User not found: newOwnerId (" + transfertUserProjects.getNewOwnerId() + ")", Status.NOT_FOUND);
			}
		} else {
			throw new WebApplicationException("Parameter is missing: newOwnerId", Status.BAD_REQUEST);
		}

		if (transfertUserProjects.getCurrentOwnerId().equals(transfertUserProjects.getNewOwnerId())) {
			throw new WebApplicationException("OwnerId references must be different", Status.BAD_REQUEST);
		}

		List<ProjectEntity> transfertAllProjects = null;
		switch (transfertUserProjects.getMode()) {
		// Transfert all projects without any condition
		case ALL: {
			transfertAllProjects = this.refProjectDAO.transfertAllProjects(transfertUserProjects.getCurrentOwnerId(),
					transfertUserProjects.getNewOwnerId());

			break;
		}
		// Transfert all projects if the projects are shared.
		case FORCED: {
			transfertAllProjects = this.refProjectDAO.transfertProjectsIfExistingCollaberators(
					transfertUserProjects.getCurrentOwnerId(), transfertUserProjects.getNewOwnerId());

			break;
		}
		// Transfert all projects to the New Owner if this one is a collaberator.
		case NORMAL:
		default: {
			transfertAllProjects = this.refProjectDAO.transfertProjectsIfNewOwnerIsCollaberator(
					transfertUserProjects.getCurrentOwnerId(), transfertUserProjects.getNewOwnerId());
		}
		}

		UserResult userResult = new UserResult();
		userResult.setOwnerProject(EntityFactory.createProjects(transfertAllProjects));
		return userResult;
	}

	@Override
	public UserResult prepareTransfertUserProjects(String id) {
		if (null == id) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		UserResult newProjectResult = new UserResult();

		// Retrieve projects from its owner id.
		final List<ProjectEntity> allProjectsByOwnerId = this.refProjectDAO.getAllProjectsByOwnerId(id);
		if (allProjectsByOwnerId != null && !allProjectsByOwnerId.isEmpty()) {
			newProjectResult.setOwnerProject(EntityFactory.createProjects(allProjectsByOwnerId));
		} else {
			newProjectResult.setOwnerProject(new ArrayList<Project>());
		}

		// Retrieve all users.
		final List<UserEntity> allUsers = refUserDAO.getAllUsers();
		if (allUsers != null) {
			newProjectResult.setData(EntityFactory.createUsers(allUsers));
		}

		return newProjectResult;
	}

	@Override
	public UserResult removeCollaberator(String id) {
		if (null == id) {
			throw new WebApplicationException(MySharelatexManagerConstant.PARAMETER_IS_MISSING_MSG, Status.BAD_REQUEST);
		}

		UserResult newProjectResult = new UserResult();

		final Optional<UserEntity> userById = this.refUserDAO.getUserById(id);
		if (!userById.isPresent()) {
			throw new WebApplicationException("User not found.", Status.NOT_FOUND);
		}

		Optional<Long> removeCollaberator = refProjectDAO.removeCollaberator(id);

		if (removeCollaberator.isPresent()) {
			newProjectResult.setRemovedCollaberatorProjectsNumber(removeCollaberator.get());
		} else {
			throw new WebApplicationException("Something is wrong.", Status.INTERNAL_SERVER_ERROR);
		}

		return newProjectResult;
	}
}
