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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ExtendWith(MockitoExtension.class)
public class ProjectDAOImplTest extends AbstractDAOTest {

	@InjectMocks
	private ProjectDAOMongo currentDAO;

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();

		// Inject specific implementation of SessionMongoTest in currentDAO.
		currentDAO.refSessionMongo = this.refSessionMongo;
	}

	@Test
	public void updateProject_withUnknownProject() {
		// Given
		final String filter = CHI2050;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		assertNotNull(allProjects);
		assertEquals(1, allProjects.size());
		ProjectEntity currentUser = allProjects.get(0);
		// Bad ID.
		currentUser.setId("555f6aba1efeb68700633359");

		// When
		final boolean updateUser = currentDAO.updateProject(currentUser);

		// Then
		assertFalse(updateUser);
	}

	@Test
	public void updateProjectRemoveCollaboratorsIdTest() {
		// Given
		final String filter = SUPER_CONF;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		assertNotNull(allProjects);
		assertEquals(1, allProjects.size());
		ProjectEntity currentProject = allProjects.get(0);
		assertEquals(SUPER_CONF_ID, currentProject.getId());
		currentProject.setDescription(CONFERENCE + CONFERENCE);
		currentProject.setName(SUPER_CONF + SUPER_CONF);
		final List<String> collaboratorsId = currentProject.getCollaboratorsId();
		assertEquals(2, collaboratorsId.size());
		collaboratorsId.remove(1);
		currentProject.setCollaboratorsId(collaboratorsId);

		// When
		final boolean updateProject = currentDAO.updateProject(currentProject);

		// Then
		assertTrue(updateProject);
		allProjects = currentDAO.getAllProjects(filter);
		assertNotNull(allProjects);
		assertEquals(1, allProjects.size());
		currentProject = allProjects.get(0);
		assertEquals(CONFERENCE + CONFERENCE, currentProject.getDescription());
		assertEquals(SUPER_CONF + SUPER_CONF, currentProject.getName());
		assertEquals(1, currentProject.getCollaboratorsId().size());
	}

	@Test
	public void updateProjectAddCollaboratorsIdTest() {
		// Given
		final String filter = CHI2050;
		List<ProjectEntity> allProjects = currentDAO.getAllProjects(filter);
		assertNotNull(allProjects);
		assertEquals(1, allProjects.size());
		ProjectEntity currentProject = allProjects.get(0);
		assertEquals(CHI2050_ID, currentProject.getId());
		currentProject.setDescription(CONFERENCE + CONFERENCE);
		currentProject.setName(CHI2050 + CHI2050);
		final List<String> collaboratorsId = currentProject.getCollaboratorsId();
		assertEquals(0, collaboratorsId.size());
		collaboratorsId.add(ZOUBIDA_ID);
		currentProject.setCollaboratorsId(collaboratorsId);

		// When
		final boolean updateProject = currentDAO.updateProject(currentProject);

		// Then
		assertTrue(updateProject);
		allProjects = currentDAO.getAllProjects(filter);
		assertNotNull(allProjects);
		assertEquals(1, allProjects.size());
		currentProject = allProjects.get(0);
		assertEquals(CONFERENCE + CONFERENCE, currentProject.getDescription());
		assertEquals(CHI2050 + CHI2050, currentProject.getName());
		assertEquals(1, currentProject.getCollaboratorsId().size());
	}

	@Test
	public void getAllProjectsTest() {
		// Given
		// Database initialization

		// When
		final List<ProjectEntity> allProjects = currentDAO.getAllProjects();

		// Then
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		final ProjectEntity project = allProjects.get(0);
		assertEquals(SUPER_CONF, project.getName());
		assertEquals(2, project.getCollaborators().size());
		assertEquals(2, project.getCollaboratorsId().size());
		assertEquals(CHI2050, allProjects.get(1).getName());
		assertEquals("Fri Jun 05 17:56:04 CEST 2015", project.getLastUpdated().toString());
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
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		assertEquals(CHI2050, allProjects.get(0).getName());
		assertEquals(SUPER_CONF, allProjects.get(1).getName());
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
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		assertEquals(CHI2050, allProjects.get(0).getName());
		assertEquals(SUPER_CONF, allProjects.get(1).getName());
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
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		assertEquals(SUPER_CONF, allProjects.get(0).getName());
		assertEquals(CHI2050, allProjects.get(1).getName());
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
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		assertEquals(SUPER_CONF, allProjects.get(0).getName());
		assertEquals(CHI2050, allProjects.get(1).getName());
	}

	@Test
	public void getAllProjectsWithFilterTest() {
		// Given
		final String filter = "chi";

		// When
		final List<ProjectEntity> allUsers = currentDAO.getAllProjects(filter);

		// Then
		assertNotNull(allUsers);
		assertEquals(1, allUsers.size());
		assertEquals(CHI2050, allUsers.get(0).getName());
	}

	@Test
	public void getAllProjectsWithFilterNoResultTest() {
		// Given
		final String filter = "blabla";

		// When
		final List<ProjectEntity> allUsers = currentDAO.getAllProjects(filter);

		// Then
		assertNotNull(allUsers);
		assertEquals(0, allUsers.size());
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
		assertNotNull(allProjects);
		assertEquals(2, allProjects.size());
		assertEquals(CHI2050, allProjects.get(0).getName());
		assertEquals(SUPER_CONF, allProjects.get(1).getName());
	}

	@Test
	public void getProjectByIdTest() {
		// Given

		// When
		final Optional<ProjectEntity> projectById = currentDAO.getProjectById(CHI2050_ID);

		// Then
		assertTrue(projectById.isPresent());
		assertEquals(CHI2050, projectById.get().getName());
	}

	@Test
	public void getAllProjectsByOwnerIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(BARON_ID);

		// Then
		assertFalse(allProjectsByOwnerId.isEmpty());
		assertEquals(2, allProjectsByOwnerId.size());
	}

	@Test
	public void getAllProjectsByOwnerIdWithNoObjectIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId("1");

		// Then
		assertTrue(allProjectsByOwnerId.isEmpty());
	}

	@Test
	public void getAllProjectsByOwnerIdWithoutResultTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(BAD_USER_ID);

		// Then
		assertTrue(allProjectsByOwnerId.isEmpty());
	}

	@Test
	public void getAllProjectsByCollaboratorsIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaboratorsId = currentDAO.getAllProjectsByCollaboratorsId(BARON_ID);

		// Then
		assertFalse(allProjectsByCollaboratorsId.isEmpty());
		assertEquals(1, allProjectsByCollaboratorsId.size());
	}

	@Test
	public void getAllProjectsByCollaboratorsIdWithNoObjectIdTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaboratorsId = currentDAO.getAllProjectsByCollaboratorsId("1");

		// Then
		assertTrue(allProjectsByCollaboratorsId.isEmpty());
	}

	@Test
	public void getAllProjectsByCollaboratorsIdWithoutResultTest() {
		// Given

		// When
		final List<ProjectEntity> allProjectsByCollaboratorsId = currentDAO
				.getAllProjectsByCollaboratorsId(BAD_USER_ID);

		// Then
		assertTrue(allProjectsByCollaboratorsId.isEmpty());
	}

	@Test
	public void updateProjectsOwnerTest() {
		// Given

		// When
		final boolean updateProjectsOwner = currentDAO.updateProjectsOwner(BARON_ID, POILU_ID);

		// Then
		assertTrue(updateProjectsOwner);
		final List<ProjectEntity> allProjectsByOwnerId = currentDAO.getAllProjectsByOwnerId(POILU_ID);
		assertNotNull(allProjectsByOwnerId);
		assertEquals(2, allProjectsByOwnerId.size());
	}

	@Test
	public void removeCollaboratorUtilTest() {
		// Given
		List<String> collaboratorsId = new ArrayList<String>();
		collaboratorsId.add(BARON_ID);
		collaboratorsId.add(POILU_ID);
		collaboratorsId.add(ZOUBIDA_ID);
		assertEquals(3, collaboratorsId.size());

		// When
		final List<String> removeCollabortorResult = ((ProjectDAOMongo) currentDAO).removeCollaboratorUtil(BARON_ID,
				collaboratorsId);

		// Then
		assertEquals(2, removeCollabortorResult.size());
	}

	@Test
	public void removeCollaboratorUtilIfEmptyTest() {
		// Given
		List<String> collaboratorsId = new ArrayList<String>();
		assertEquals(0, collaboratorsId.size());

		// When
		final List<String> removeCollabortorResult = ((ProjectDAOMongo) currentDAO).removeCollaboratorUtil(BARON_ID,
				collaboratorsId);

		// Then
		assertEquals(0, removeCollabortorResult.size());
	}

	@Test
	public void removeCollaboratorIfNotFoundTest() {
		// Given
		List<String> collaboratorsId = new ArrayList<String>();
		collaboratorsId.add(MySharelatexManagerTestConstant.POILU_ID);
		assertEquals(1, collaboratorsId.size());

		// When
		final List<String> removeCollabortorResult = ((ProjectDAOMongo) currentDAO).removeCollaboratorUtil(BARON_ID,
				collaboratorsId);

		// Then
		assertEquals(1, removeCollabortorResult.size());
	}

	@Test
	public void transferAllProjectsTest() {
		// Given

		// When
		final List<ProjectEntity> transferAllProjects = currentDAO.transferAllProjects(BARON_ID, ZOUBIDA_ID);

		// Then
		assertEquals(0, transferAllProjects.size());
	}

	@Test
	public void transferProjectsIfExistingCollaboratorsTest() {
		// Given

		// When
		final List<ProjectEntity> transferAllProjects = currentDAO.transferProjectsIfExistingCollaborators(BARON_ID,
				ZOUBIDA_ID);

		// Then
		assertEquals(1, transferAllProjects.size());
	}

	@Test
	public void transferProjectsIfNewOwnerIsCollaboratorWithNoMatchCollaboratorTest() {
		// Given

		// When
		final List<ProjectEntity> transferAllProjects = currentDAO.transferProjectsIfNewOwnerIsCollaborator(BARON_ID,
				ZOUBIDA_ID);

		// Then
		assertEquals(2, transferAllProjects.size());
	}

	@Test
	public void transferProjectsIfNewOwnerIsCollaboratorWithMatchCollaboratorTest() {
		// Given

		// When
		final List<ProjectEntity> transferAllProjects = currentDAO.transferProjectsIfNewOwnerIsCollaborator(BARON_ID,
				POILU_ID);

		// Then
		assertEquals(1, transferAllProjects.size());
	}

	@Test
	public void isCollaboratorPresentTest() {
		// Given
		List<String> collaboratorsId = new ArrayList<String>();
		collaboratorsId.add(BARON_ID);
		collaboratorsId.add(POILU_ID);
		collaboratorsId.add(ZOUBIDA_ID);
		assertEquals(3, collaboratorsId.size());

		// When
		final boolean collaboratorPresent = ((ProjectDAOMongo) currentDAO).isCollaboratorPresent(BARON_ID,
				collaboratorsId);

		// Then
		assertTrue(collaboratorPresent);
	}

	@Test
	public void isCollaboratorNotPresentTest() {
		// Given
		List<String> collaboratorsId = new ArrayList<String>();
		collaboratorsId.add(POILU_ID);
		collaboratorsId.add(ZOUBIDA_ID);
		assertEquals(2, collaboratorsId.size());

		// When
		final boolean collaboratorPresent = ((ProjectDAOMongo) currentDAO).isCollaboratorPresent(BARON_ID,
				collaboratorsId);

		// Then
		assertFalse(collaboratorPresent);
	}

	@Test
	public void removeCollaboratorTest() {
		// Given

		// When
		Optional<Long> removeCollaborator = currentDAO.removeCollaborator(POILU_ID);

		// Then
		assertTrue(removeCollaborator.isPresent());
		assertEquals(Long.valueOf(1), removeCollaborator.get());
		final List<ProjectEntity> allProjectsByCollaboratorsId = currentDAO.getAllProjectsByCollaboratorsId(POILU_ID);
		assertNotNull(allProjectsByCollaboratorsId);
		assertEquals(0, allProjectsByCollaboratorsId.size());
	}
}
