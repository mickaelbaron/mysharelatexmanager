package fr.mickaelbaron.mysharelatexmanager.service;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.DESCRIPTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.NAME;
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

import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant;
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
@RunWith(CdiRunner.class)
public class ProjectResourceImplTest extends AbstractResourceTest {

	@Inject
	private ProjectResourceImpl currentResource;

	@Produces
	@Mock
	IProjectDAO projectDAO;

	@Produces
	@Mock
	IUserDAO userDAO;

	@Test
	public void updateProjectWithoutParameterTest() {
		// Given

		// When and Then
		try {
			currentResource.updateProject(null);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void updateProjectWithUnmodifiedProjectTest() {
		// Given
		Mockito.when(projectDAO.updateProject(any(ProjectEntity.class))).thenReturn(false);
		final ProjectEntity project = createAscProjects().get(0);
		Mockito.when(projectDAO.getProjectById(project.getId())).thenReturn(Optional.of(project));

		// When 
		Response updateProject = currentResource.updateProject(EntityFactory.createProject(project));

		// Then
		Assert.assertEquals(204, updateProject.getStatus());
	}

	@Test
	public void updateProjectWithNotFoundProjectTest() {
		// Given
		Mockito.when(projectDAO.getProjectById(any(String.class))).thenReturn(Optional.empty());

		// When and Then
		try {
			currentResource.updateProject(new Project());
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals(MySharelatexManagerConstant.PROJECT_NOT_FOUND_MSG, e.getMessage());
			Assert.assertEquals(Status.NOT_FOUND, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void updateProjectTest() {
		// Given
		Mockito.when(projectDAO.updateProject(any(ProjectEntity.class))).thenReturn(true);
		final ProjectEntity project = createAscProjects().get(0);
		Mockito.when(projectDAO.getProjectById(project.getId())).thenReturn(Optional.of(project));

		// When
		final Response updateProject = currentResource.updateProject(EntityFactory.createProject(project));

		// Then
		Assert.assertEquals(200, updateProject.getStatus());
	}

	@Test
	public void getAllProjectsTest() {
		// Given
		Mockito.when(projectDAO.getAllProjects(new ArrayList<SortedData>(), "")).thenReturn(createAscProjects());

		// When
		final ProjectResult allProjects = currentResource.getAllProjects("", "");

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals((Integer) 2, allProjects.getTotal());
		Assert.assertEquals(CHI2050, allProjects.getData().get(0).getName());
		Assert.assertEquals(SUPER_CONF, allProjects.getData().get(1).getName());
	}

	@Test
	public void getAllProjectsWithAscSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(NAME, true));

		Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createAscProjects());

		// When
		String sorted = NAME + "|asc";
		final ProjectResult allProjects = currentResource.getAllProjects(sorted, "");

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals((Integer) 2, allProjects.getTotal());
		Assert.assertEquals(CHI2050, allProjects.getData().get(0).getName());
		Assert.assertEquals(SUPER_CONF, allProjects.getData().get(1).getName());
	}

	@Test
	public void getAllProjectsWithMultipleAscSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(DESCRIPTION, true));
		sortedData.add(new SortedData(NAME, true));

		Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createAscProjects());

		// When
		String sorted = DESCRIPTION + "|asc," + NAME + "|asc";
		final ProjectResult allUsers = currentResource.getAllProjects(sorted, "");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(CHI2050, allUsers.getData().get(0).getName());
		Assert.assertEquals(SUPER_CONF, allUsers.getData().get(1).getName());
	}

	@Test
	public void getAllProjectsWithDescSorterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(NAME, false));

		Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createDescProjects());

		// When
		String sorted = NAME + "|desc";
		final ProjectResult allProjects = currentResource.getAllProjects(sorted, "");

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals((Integer) 2, allProjects.getTotal());
		Assert.assertEquals(SUPER_CONF, allProjects.getData().get(0).getName());
		Assert.assertEquals(CHI2050, allProjects.getData().get(1).getName());
	}

	@Test
	public void getAllUsersWithAscSorterAndFilterTest() {
		// Given
		List<SortedData> sortedData = new ArrayList<SortedData>();
		sortedData.add(new SortedData(NAME, true));

		Mockito.when(projectDAO.getAllProjects(sortedData, "Conference")).thenReturn(createAscProjects());

		// When
		String sorted = NAME + "|asc";
		final ProjectResult allUsers = currentResource.getAllProjects(sorted, "Conference");

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals((Integer) 2, allUsers.getTotal());
		Assert.assertEquals(CHI2050, allUsers.getData().get(0).getName());
		Assert.assertEquals(SUPER_CONF, allUsers.getData().get(1).getName());
	}
	
	@Test
	public void prepareUpdateProjectWithoutParameterTest() {
		// Given

		// When and Then
		try {
			currentResource.prepareUpdateProject(null);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void prepareUpdateProjectWithUnknownProjectIdTest() {
		// Given
		Mockito.when(projectDAO.getProjectById(any(String.class))).thenReturn(Optional.empty());

		// When and Then
		try {
			currentResource.prepareUpdateProject("FAKE");
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Project not found.", e.getMessage());
			Assert.assertEquals(Status.NO_CONTENT, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void prepareUpdateProjectTest() {
		// Given
		Mockito.when(projectDAO.getProjectById(CHI2050_ID)).thenReturn(Optional.of(createAscProjects().get(0)));
		Mockito.when(userDAO.getAllUsers()).thenReturn(this.createAscUsers());

		// When
		final ProjectResult prepareUpdateProject = currentResource.prepareUpdateProject(CHI2050_ID);

		// Then
		Assert.assertNotNull(prepareUpdateProject);
		Assert.assertNotNull(prepareUpdateProject.getUsers());
		Assert.assertNotNull(prepareUpdateProject.getUpdateProject());
	}
}
