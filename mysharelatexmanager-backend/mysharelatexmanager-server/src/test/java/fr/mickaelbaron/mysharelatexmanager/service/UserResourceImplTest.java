package fr.mickaelbaron.mysharelatexmanager.service;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BAD_USER_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA_ID;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_NAME;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.EntityFactory;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;
import fr.mickaelbaron.mysharelatexmanager.model.TransfertUserProjects;
import fr.mickaelbaron.mysharelatexmanager.model.TransfertUserProjects.MODE_ENUM;
import fr.mickaelbaron.mysharelatexmanager.model.User;
import fr.mickaelbaron.mysharelatexmanager.model.UserResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(CdiRunner.class)
public class UserResourceImplTest extends AbstractResourceTest {

	@Inject
	private UserResourceImpl currentResource;

	@Produces
	@Mock
	IUserDAO userDAO;

	@Produces
	@Mock
	IProjectDAO projectDAO;

	@Test
	public void updateUserWithoutParameterTest() {
		// Given

		// When and Then
		try {
			currentResource.updateUser(null);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void updateUserWithUnmodifiedUserTest() {
		// Given
		Mockito.when(userDAO.updateUser(any(UserEntity.class))).thenReturn(false);
		UserEntity user = createAscUsers().get(0);
		Mockito.when(userDAO.getUserById(user.getId())).thenReturn(Optional.of(user));

		// When
		Response updateUser = currentResource.updateUser(EntityFactory.createUser(user));
		
		// Then
		Assert.assertEquals(204, updateUser.getStatus());
	}

	@Test
	public void updateUserWithNotFoundUserAfterModifyingUserTest() {
		// Given
		Mockito.when(userDAO.updateUser(any(UserEntity.class))).thenReturn(true);
		Mockito.when(userDAO.getUserById(any(String.class))).thenReturn(Optional.empty());

		// When and Then
		try {
			currentResource.updateUser(new User());
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("User not found.", e.getMessage());
			Assert.assertEquals(Status.NOT_FOUND, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void updateUserTest() {
		// Given
		Mockito.when(userDAO.updateUser(any(UserEntity.class))).thenReturn(true);
		final UserEntity user = createAscUsers().get(0);
		Mockito.when(userDAO.getUserById(user.getId())).thenReturn(Optional.of(user));

		// When
		final Response updateUser = currentResource.updateUser(EntityFactory.createUser(user));

		// Then
		Assert.assertEquals(200, updateUser.getStatus());
	}

	@Test
	public void getAllUsersTest() {
		// Given
		Mockito.when(userDAO.getAllUsers(new ArrayList<SortedData>(), "")).thenReturn(createAscUsers());

		// When
		final UserResult allUsers = currentResource.getAllUsers("", "");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
	}

	@Test
	public void getAllUsersWithAscSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(LAST_NAME, true));

		Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createAscUsers());

		// When
		String sorted = LAST_NAME + "|asc";
		final UserResult allUsers = currentResource.getAllUsers(sorted, "");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(BARON, allUsers.getData().get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.getData().get(1).getLastName());
	}

	@Test
	public void getAllUsersWithMultipleAscSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(EMAIL, true));
		sortedData.add(new SortedData(LAST_NAME, true));

		Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createAscUsers());

		// When
		String sorted = EMAIL + "|asc," + LAST_NAME + "|asc";
		final UserResult allUsers = currentResource.getAllUsers(sorted, "");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(BARON, allUsers.getData().get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.getData().get(1).getLastName());
	}

	@Test
	public void getAllUsersWithDescSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(LAST_NAME, false));

		Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createDescUsers());

		// When
		String sorted = LAST_NAME + "|desc";
		final UserResult allUsers = currentResource.getAllUsers(sorted, "");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(POILU, allUsers.getData().get(0).getLastName());
		Assert.assertEquals(BARON, allUsers.getData().get(1).getLastName());
	}

	@Test
	public void getAllUsersWithAscSorterAndFilterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(LAST_NAME, true));

		Mockito.when(userDAO.getAllUsers(sortedData, "baron")).thenReturn(createAscUsers());

		// When
		String sorted = LAST_NAME + "|asc";
		final UserResult allUsers = currentResource.getAllUsers(sorted, "baron");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(BARON, allUsers.getData().get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.getData().get(1).getLastName());
	}

	@Test
	public void deleteUserTest() {
		// Given
		Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
		Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
		Mockito.when(projectDAO.getAllProjectsByCollaberatorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
		Mockito.when(userDAO.deleteUser(BARON_ID)).thenReturn(true);

		// When
		final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

		// Then
		Assert.assertEquals(0, deleteUser.getCollaberatorsProject().size());
		Assert.assertEquals(0, deleteUser.getOwnerProject().size());
	}

	@Test
	public void deleteUserWithProbemTest() {
		// Given
		Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
		Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
		Mockito.when(projectDAO.getAllProjectsByCollaberatorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
		Mockito.when(userDAO.deleteUser(BARON_ID)).thenReturn(false);

		// When and Then
		try {
			currentResource.deleteUser(BARON_ID);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			Assert.assertEquals("Cannot delete user: " + BARON_ID, e.getMessage());
			Assert.assertEquals(Status.NOT_FOUND, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void deleteUserWithoutParameterTest() {
		// Given

		// When and Then
		try {
			currentResource.deleteUser(null);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void deleteUserWithBadUserIDTest() {
		// Given
		Mockito.when(userDAO.getUserById(BAD_USER_ID)).thenReturn(Optional.empty());

		// When and Then
		try {
			currentResource.deleteUser(BAD_USER_ID);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("User not found.", e.getMessage());
			Assert.assertEquals(Status.NOT_FOUND, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void deleteUserWithExistingOwnerProjectsTest() {
		// Given
		Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
		final List<ProjectEntity> createAscProjects = this.createAscProjects();
		Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(createAscProjects);
		Mockito.when(projectDAO.getAllProjectsByCollaberatorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());

		// When
		final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

		// Then
		Assert.assertEquals(2, deleteUser.getOwnerProject().size());
		Assert.assertEquals(0, deleteUser.getCollaberatorsProject().size());
	}

	@Test
	public void deleteUserWithExistingCollaberatorsProjectsTest() {
		// Given
		Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
		final List<ProjectEntity> createAscProjects = this.createAscProjects();
		Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
		Mockito.when(projectDAO.getAllProjectsByCollaberatorsId(BARON_ID)).thenReturn(createAscProjects);

		// When
		final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

		// Then
		Assert.assertEquals(0, deleteUser.getOwnerProject().size());
		Assert.assertEquals(2, deleteUser.getCollaberatorsProject().size());
	}

	@Test
	public void deleteUserWithExistingBothOwnerAndCollaberatorProjectsTest() {
		// Given
		Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
		final List<ProjectEntity> createAscProjects = this.createAscProjects();
		Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(createAscProjects);
		Mockito.when(projectDAO.getAllProjectsByCollaberatorsId(BARON_ID)).thenReturn(createAscProjects);

		// When
		final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

		// Then
		Assert.assertEquals(2, deleteUser.getOwnerProject().size());
		Assert.assertEquals(2, deleteUser.getCollaberatorsProject().size());
	}

	@Test
	public void transfertUserProjectsWithoutParameterTest() {
		// Given

		// When and Then
		try {
			currentResource.transfertUserProjects(null);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void transfertUserProjectsWithoutCurrentOwnerIdTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setMode(MODE_ENUM.ALL);
		newDTO.setCurrentOwnerId(null);

		// When and Then
		try {
			currentResource.transfertUserProjects(newDTO);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing: currentOwnerId", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void transfertUserProjectsWithoutModeTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId(BARON_ID);
		newDTO.setNewOwnerId(ZOUBIDA_ID);
		newDTO.setMode(null);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));

		// When and Then
		try {
			currentResource.transfertUserProjects(newDTO);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing: mode", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void transfertUserProjectsWithTheSameOwnerIdsTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId(BARON_ID);
		newDTO.setNewOwnerId(BARON_ID);
		newDTO.setMode(MODE_ENUM.ALL);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));

		// When and Then
		try {
			currentResource.transfertUserProjects(newDTO);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("OwnerId references must be different", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void transfertUserProjectsWithoutNewOwnerIdTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId("abcde");
		newDTO.setNewOwnerId(null);
		newDTO.setMode(MODE_ENUM.ALL);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));

		// When and Then
		try {
			currentResource.transfertUserProjects(newDTO);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing: newOwnerId", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void transfertUserProjectsAllTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId(BARON_ID);
		newDTO.setNewOwnerId(ZOUBIDA_ID);
		newDTO.setMode(MODE_ENUM.ALL);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(projectDAO.transfertAllProjects(newDTO.getCurrentOwnerId(), newDTO.getNewOwnerId()))
				.thenReturn(new ArrayList<ProjectEntity>());

		// When
		final UserResult transfertUserProjects = currentResource.transfertUserProjects(newDTO);

		// Then
		Assert.assertNotNull(transfertUserProjects);
		Assert.assertEquals(0, transfertUserProjects.getOwnerProject().size());
	}
	
	@Test
	public void transfertUserProjectsForcedTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId(BARON_ID);
		newDTO.setNewOwnerId(ZOUBIDA_ID);
		newDTO.setMode(MODE_ENUM.FORCED);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(projectDAO.transfertProjectsIfExistingCollaberators(newDTO.getCurrentOwnerId(), newDTO.getNewOwnerId()))
				.thenReturn(new ArrayList<ProjectEntity>());

		// When
		final UserResult transfertUserProjects = currentResource.transfertUserProjects(newDTO);

		// Then
		Assert.assertNotNull(transfertUserProjects);
		Assert.assertEquals(0, transfertUserProjects.getOwnerProject().size());
	}

	@Test
	public void transfertUserProjectsNormalTest() {
		// Given
		TransfertUserProjects newDTO = new TransfertUserProjects();
		newDTO.setCurrentOwnerId(BARON_ID);
		newDTO.setNewOwnerId(ZOUBIDA_ID);
		newDTO.setMode(MODE_ENUM.FORCED);

		Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
		Mockito.when(projectDAO.transfertProjectsIfNewOwnerIsCollaberator(newDTO.getCurrentOwnerId(), newDTO.getNewOwnerId()))
				.thenReturn(new ArrayList<ProjectEntity>());

		// When
		final UserResult transfertUserProjects = currentResource.transfertUserProjects(newDTO);

		// Then
		Assert.assertNotNull(transfertUserProjects);
		Assert.assertEquals(0, transfertUserProjects.getOwnerProject().size());
	}
	
	@Test
	public void prepareTransfertUserProjectsWithoutParameterTest() {
		// Given
		String rid = null;
		
		// When
		try {
			currentResource.prepareTransfertUserProjects(rid);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}
}
