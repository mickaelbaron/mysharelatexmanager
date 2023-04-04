package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BAD_USER_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CONFERENCE;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA_ID;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.DESCRIPTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant;
import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({ ProjectDAOMongo.class, SessionMongoTest.class })
public class ProjectDAOImplTest extends AbstractDAOTest {

	@Inject
	private IProjectDAO currentDAO;

	@Test
	public void updateProjectWithUnknownProjectTest() {
		// Given
		final String filter = CHI2050;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(1, allProjects.size());
		ProjectEntity currentUser = allProjects.get(0);
		// Bad ID.
		currentUser.setId("555f6aba1efeb68700633359");

		// When
		final boolean updateUser = currentDAO.updateProject(currentUser);

		// Then
		Assert.assertFalse(updateUser);
	}

	@Test
	public void updateProjectRemoveCollaberatorsIdTest() {
		// Given
		final String filter = SUPER_CONF;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(1, allProjects.size());
		ProjectEntity currentProject = allProjects.get(0);
		Assert.assertEquals(SUPER_CONF_ID, currentProject.getId());
		currentProject.setDescription(CONFERENCE + CONFERENCE);
		currentProject.setName(SUPER_CONF + SUPER_CONF);
		final List<String> collaberatorsId = currentProject.getCollaberatorsId();
		Assert.assertEquals(2, collaberatorsId.size());
		collaberatorsId.remove(1);
		currentProject.setCollaberatorsId(collaberatorsId);

		// When
		final boolean updateProject = currentDAO.updateProject(currentProject);

		// Then
		Assert.assertTrue(updateProject);
		allProjects = currentDAO.getAllProjects(filter);
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(1, allProjects.size());
		currentProject = allProjects.get(0);
		Assert.assertEquals(CONFERENCE + CONFERENCE, currentProject.getDescription());
		Assert.assertEquals(SUPER_CONF + SUPER_CONF, currentProject.getName());
		Assert.assertEquals(1, currentProject.getCollaberatorsId().size());
	}

	@Test
	public void updateProjectAddCollaberatorsIdTest() {
		// Given
		final String filter = CHI2050;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(1, allProjects.size());
		ProjectEntity currentProject = allProjects.get(0);
		Assert.assertEquals(CHI2050_ID, currentProject.getId());
		currentProject.setDescription(CONFERENCE + CONFERENCE);
		currentProject.setName(CHI2050 + CHI2050);
		final List<String> collaberatorsId = currentProject.getCollaberatorsId();
		Assert.assertEquals(0, collaberatorsId.size());
		collaberatorsId.add(ZOUBIDA_ID);
		currentProject.setCollaberatorsId(collaberatorsId);

		// When
		final boolean updateProject = currentDAO.updateProject(currentProject);

		// Then
		Assert.assertTrue(updateProject);
		allProjects = currentDAO.getAllProjects(filter);
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(1, allProjects.size());
		currentProject = allProjects.get(0);
		Assert.assertEquals(CONFERENCE + CONFERENCE, currentProject.getDescription());
		Assert.assertEquals(CHI2050 + CHI2050, currentProject.getName());
		Assert.assertEquals(1, currentProject.getCollaberatorsId().size());
	}

	@Test
	public void getAllProjectsTest() {
		// Given
		// Database initialization

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects();
		
		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		final ProjectEntity project = allProjects.get(0);
		Assert.assertEquals(SUPER_CONF, project.getName());
		Assert.assertEquals(2, project.getCollaberators().size());
		Assert.assertEquals(2, project.getCollaberatorsId().size());
		Assert.assertEquals(CHI2050, allProjects.get(1).getName());
		Assert.assertEquals("Fri Jun 05 17:56:04 CEST 2015", project.getLastUpdated().toString());
	}

	@Test
	public void getAllProjectsWithAscSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(NAME, true));

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects(sorts);

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		Assert.assertEquals(CHI2050, allProjects.get(0).getName());
		Assert.assertEquals(SUPER_CONF, allProjects.get(1).getName());
	}

	@Test
	public void getAllProjectsWithMultipleAscSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(DESCRIPTION, true));
		sorts.add(new SortedData(NAME, true));

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects(sorts);

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		Assert.assertEquals(CHI2050, allProjects.get(0).getName());
		Assert.assertEquals(SUPER_CONF, allProjects.get(1).getName());
	}

	@Test
	public void getAllProjectsWithDescSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(NAME, false));

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects(sorts);

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		Assert.assertEquals(SUPER_CONF, allProjects.get(0).getName());
		Assert.assertEquals(CHI2050, allProjects.get(1).getName());
	}

	@Test
	public void getAllProjectsWithMultipleDescSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(DESCRIPTION, false));
		sorts.add(new SortedData(NAME, false));

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects(sorts);

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		Assert.assertEquals(SUPER_CONF, allProjects.get(0).getName());
		Assert.assertEquals(CHI2050, allProjects.get(1).getName());
	}

	@Test
	public void getAllProjectsWithFilterTest() {
		// Given
		final String filter = "chi";

		// When
		final List<ProjectEntity> allUsers = currentDAO.getAllProjects(filter);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(1, allUsers.size());
		Assert.assertEquals(CHI2050, allUsers.get(0).getName());
	}

	@Test
	public void getAllProjectsWithFilterNoResultTest() {
		// Given
		final String filter = "blabla";

		// When
		final List<ProjectEntity> allUsers = currentDAO.getAllProjects(filter);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(0, allUsers.size());
	}

	@Test
	public void getAllProjectsWithAscSorterAndFilterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(NAME, true));
		final String filter = "conference";

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects(sorts, filter);

		// Then
		Assert.assertNotNull(allProjects);
		Assert.assertEquals(2, allProjects.size());
		Assert.assertEquals(CHI2050, allProjects.get(0).getName());
		Assert.assertEquals(SUPER_CONF, allProjects.get(1).getName());
	}

	@Test
	public void getProjectByIdTest() {
		// Given

		// When
		final Optional<ProjectEntity> projectById = currentDAO.getProjectById(CHI2050_ID);

		// Then
		Assert.assertTrue(projectById.isPresent());
		Assert.assertEquals(CHI2050, projectById.get().getName());
	}

	@Test
	public void getAllProjectsByOwnerIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(BARON_ID);

		// Then
		Assert.assertFalse(allProjectsByOwnerId.isEmpty());
		Assert.assertEquals(2, allProjectsByOwnerId.size());
	}

	@Test
	public void getAllProjectsByOwnerIdWithNoObjectIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId("1");

		// Then
		Assert.assertTrue(allProjectsByOwnerId.isEmpty());
	}

	@Test
	public void getAllProjectsByOwnerIdWithoutResultTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(BAD_USER_ID);

		// Then
		Assert.assertTrue(allProjectsByOwnerId.isEmpty());
	}

	@Test
	public void getAllProjectsByCollaberatorsIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaberatorsId = currentDAO.getAllProjectsByCollaberatorsId(BARON_ID);

		// Then
		Assert.assertFalse(allProjectsByCollaberatorsId.isEmpty());
		Assert.assertEquals(1, allProjectsByCollaberatorsId.size());
	}

	@Test
	public void getAllProjectsByCollaberatorsIdWithNoObjectIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaberatorsId = currentDAO.getAllProjectsByCollaberatorsId("1");

		// Then
		Assert.assertTrue(allProjectsByCollaberatorsId.isEmpty());
	}

	@Test
	public void getAllProjectsByCollaberatorsIdWithoutResultTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaberatorsId = currentDAO.getAllProjectsByCollaberatorsId(BAD_USER_ID);

		// Then
		Assert.assertTrue(allProjectsByCollaberatorsId.isEmpty());
	}

	@Test
	public void updateProjectsOwnerTest() {
		// Given

		// When
		final boolean updateProjectsOwner = currentDAO.updateProjectsOwner(BARON_ID, POILU_ID);

		// Then
		Assert.assertTrue(updateProjectsOwner);
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(POILU_ID);
		Assert.assertNotNull(allProjectsByOwnerId);
		Assert.assertEquals(2, allProjectsByOwnerId.size());
	}

	@Test
	public void removeCollaberatorUtilTest() {
		// Given
		List<String> collaberatorsId = new ArrayList<String>();
		collaberatorsId.add(BARON_ID);
		collaberatorsId.add(POILU_ID);
		collaberatorsId.add(ZOUBIDA_ID);
		Assert.assertEquals(3, collaberatorsId.size());

		// When
		final List<String> removeCollabertorResult = ((ProjectDAOMongo) currentDAO).removeCollaberatorUtil(BARON_ID,
				collaberatorsId);

		// Then
		Assert.assertEquals(2, removeCollabertorResult.size());
	}

	@Test
	public void removeCollaberatorUtilIfEmptyTest() {
		// Given
		List<String> collaberatorsId = new ArrayList<String>();
		Assert.assertEquals(0, collaberatorsId.size());

		// When
		final List<String> removeCollabertorResult = ((ProjectDAOMongo) currentDAO).removeCollaberatorUtil(BARON_ID,
				collaberatorsId);

		// Then
		Assert.assertEquals(0, removeCollabertorResult.size());
	}

	@Test
	public void removeCollaberatorIfNotFoundTest() {
		// Given
		List<String> collaberatorsId = new ArrayList<String>();
		collaberatorsId.add(MySharelatexManagerTestConstant.POILU_ID);
		Assert.assertEquals(1, collaberatorsId.size());

		// When
		final List<String> removeCollabertorResult = ((ProjectDAOMongo) currentDAO).removeCollaberatorUtil(BARON_ID,
				collaberatorsId);

		// Then
		Assert.assertEquals(1, removeCollabertorResult.size());
	}

	@Test
	public void transfertAllProjectsTest() {
		// Given

		// When
		final List<ProjectEntity> transfertAllProjects = currentDAO.transfertAllProjects(BARON_ID, ZOUBIDA_ID);

		// Then
		Assert.assertEquals(0, transfertAllProjects.size());
	}

	@Test
	public void transfertProjectsIfExistingCollaberatorsTest() {
		// Given

		// When
		final List<ProjectEntity> transfertAllProjects = currentDAO.transfertProjectsIfExistingCollaberators(BARON_ID,
				ZOUBIDA_ID);

		// Then
		Assert.assertEquals(1, transfertAllProjects.size());
	}

	@Test
	public void transfertProjectsIfNewOwnerIsCollaberatorWithNoMatchCollaberatorTest() {
		// Given

		// When
		final List<ProjectEntity> transfertAllProjects = currentDAO.transfertProjectsIfNewOwnerIsCollaberator(BARON_ID,
				ZOUBIDA_ID);

		// Then
		Assert.assertEquals(2, transfertAllProjects.size());
	}

	@Test
	public void transfertProjectsIfNewOwnerIsCollaberatorWithMatchCollaberatorTest() {
		// Given

		// When
		final List<ProjectEntity> transfertAllProjects = currentDAO.transfertProjectsIfNewOwnerIsCollaberator(BARON_ID,
				POILU_ID);

		// Then
		Assert.assertEquals(1, transfertAllProjects.size());
	}

	@Test
	public void isCollaberatorPresentTest() {
		// Given
		List<String> collaberatorsId = new ArrayList<String>();
		collaberatorsId.add(BARON_ID);
		collaberatorsId.add(POILU_ID);
		collaberatorsId.add(ZOUBIDA_ID);
		Assert.assertEquals(3, collaberatorsId.size());
		
		// When
		final boolean collaberatorPresent = ((ProjectDAOMongo) currentDAO).isCollaberatorPresent(BARON_ID, collaberatorsId);
		
		// Then
		Assert.assertTrue(collaberatorPresent);
	}
	
	@Test
	public void isCollaberatorNotPresentTest() {
		// Given
		List<String> collaberatorsId = new ArrayList<String>();
		collaberatorsId.add(POILU_ID);
		collaberatorsId.add(ZOUBIDA_ID);
		Assert.assertEquals(2, collaberatorsId.size());
		
		// When
		final boolean collaberatorPresent = ((ProjectDAOMongo) currentDAO).isCollaberatorPresent(BARON_ID, collaberatorsId);
		
		// Then
		Assert.assertFalse(collaberatorPresent);
	}
	
	@Test
	public void removeCollaberatorTest() {
		// Given
		
		// When
		Optional<Long> removeCollaberator = currentDAO.removeCollaberator(POILU_ID);
		
		// Then
		Assert.assertTrue(removeCollaberator.isPresent());
		Assert.assertEquals(Long.valueOf(1), removeCollaberator.get());
		final List<ProjectEntity> allProjectsByCollaberatorsId = currentDAO.getAllProjectsByCollaberatorsId(POILU_ID);
		Assert.assertNotNull(allProjectsByCollaberatorsId);
		Assert.assertEquals(0, allProjectsByCollaberatorsId.size());
	}
}

