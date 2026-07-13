package fr.mickaelbaron.mysharelatexmanager.service;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.DESCRIPTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant;
import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.EntityFactory;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.ProjectResult;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ExtendWith(MockitoExtension.class)
public class ProjectResourceImplTest extends AbstractServiceTest {

    @InjectMocks
    private ProjectResourceImpl currentResource;

    @Produces
    @Mock
    IProjectDAO projectDAO;

    @Produces
    @Mock
    IUserDAO userDAO;

    @Test
    public void updateProject_missingParameter() {
        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.updateProject(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    void updateProject_withoutUpdatedProject() {
        // Given
        when(projectDAO.updateProject(any(ProjectEntity.class))).thenReturn(false);
        final ProjectEntity project = createAscProjects().get(0);

        when(projectDAO.getProjectById(project.getId()))
                .thenReturn(Optional.of(project));

        // When
        Response updateProject = currentResource.updateProject(
                EntityFactory.createProject(project));

        // Then
        assertEquals(204, updateProject.getStatus());
    }

    @Test
    public void updateProject_withNullProjectID() {
        // Given
        when(projectDAO.getProjectById(nullable(String.class))).thenReturn(Optional.empty());

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.updateProject(new Project()));

        // Then
        assertEquals(
                MySharelatexManagerConstant.PROJECT_NOT_FOUND_MSG,
                exception.getMessage());
        assertEquals(
                Status.NOT_FOUND,
                exception.getResponse().getStatusInfo());
    }

    @Test
    public void updateProject_success() {
        // Given
        when(projectDAO.updateProject(any(ProjectEntity.class))).thenReturn(true);

        final ProjectEntity project = createAscProjects().get(0);
        when(projectDAO.getProjectById(project.getId()))
                .thenReturn(Optional.of(project));

        // When
        Response response = currentResource.updateProject(
                EntityFactory.createProject(project));

        // Then
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getAllProjects_success() {
        // Given
        Mockito.when(projectDAO.getAllProjects(new ArrayList<SortedData>(), "")).thenReturn(createAscProjects());

        // When
        final ProjectResult allProjects = currentResource.getAllProjects("", "");

        // Then
        assertNotNull(allProjects);
        assertEquals((Integer) 2, allProjects.getTotal());
        assertEquals(CHI2050, allProjects.getData().get(0).getName());
        assertEquals(SUPER_CONF, allProjects.getData().get(1).getName());
    }

    @Test
    public void getAllProjects_withAscSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(NAME, true));

        Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createAscProjects());
        String sorted = NAME + "|asc";

        // When
        final ProjectResult allProjects = currentResource.getAllProjects(sorted, "");

        // Then
        assertNotNull(allProjects);
        assertEquals((Integer) 2, allProjects.getTotal());
        assertEquals(CHI2050, allProjects.getData().get(0).getName());
        assertEquals(SUPER_CONF, allProjects.getData().get(1).getName());
    }

    @Test
    public void getAllProjects_withMultipleAscSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(DESCRIPTION, true));
        sortedData.add(new SortedData(NAME, true));

        Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createAscProjects());
        String sorted = DESCRIPTION + "|asc," + NAME + "|asc";

        // When
        final ProjectResult allUsers = currentResource.getAllProjects(sorted, "");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(CHI2050, allUsers.getData().get(0).getName());
        assertEquals(SUPER_CONF, allUsers.getData().get(1).getName());
    }

    @Test
    public void getAllProjects_withDescSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(NAME, false));

        Mockito.when(projectDAO.getAllProjects(sortedData, "")).thenReturn(createDescProjects());

        // When
        String sorted = NAME + "|desc";
        final ProjectResult allProjects = currentResource.getAllProjects(sorted, "");

        // Then
        assertNotNull(allProjects);
        assertEquals((Integer) 2, allProjects.getTotal());
        assertEquals(SUPER_CONF, allProjects.getData().get(0).getName());
        assertEquals(CHI2050, allProjects.getData().get(1).getName());
    }

    @Test
    public void getAllProjects_withAscSorterAndFilter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(NAME, true));

        Mockito.when(projectDAO.getAllProjects(sortedData, "Conference")).thenReturn(createAscProjects());
        String sorted = NAME + "|asc";

        // When
        final ProjectResult allUsers = currentResource.getAllProjects(sorted, "Conference");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(CHI2050, allUsers.getData().get(0).getName());
        assertEquals(SUPER_CONF, allUsers.getData().get(1).getName());
    }

    @Test
    public void getAllProjects_withNullParametersAndNoProjects() {
        // Given
        Mockito.when(projectDAO.getAllProjects(new ArrayList<SortedData>(), "")).thenReturn(null);

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.getAllProjects(null, null));

        // Then
        assertEquals("Something is wrong.", exception.getMessage());
        assertEquals(Status.INTERNAL_SERVER_ERROR, exception.getResponse().getStatusInfo());
    }

    @Test
    public void getAllProjects_withNullParameters() {
        // Given
        Mockito.when(projectDAO.getAllProjects(new ArrayList<SortedData>(), "")).thenReturn(createAscProjects());

        // When
        final ProjectResult allUsers = currentResource.getAllProjects(null, null);

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(CHI2050, allUsers.getData().get(0).getName());
        assertEquals(SUPER_CONF, allUsers.getData().get(1).getName());
    }

    @Test
    public void prepareUpdateProject_missingParameter() {
        // Given

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.prepareUpdateProject(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(
                Status.BAD_REQUEST,
                exception.getResponse().getStatusInfo());
    }

    @Test
    public void prepareUpdateProject_withUnknownProjectId() {
        // Given
        when(projectDAO.getProjectById(any(String.class)))
                .thenReturn(Optional.empty());

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.prepareUpdateProject("FAKE"));

        // Then
        assertEquals("Project not found.", exception.getMessage());
        assertEquals(Status.NO_CONTENT, exception.getResponse().getStatusInfo());
    }

    @Test
    public void prepareUpdateProject_success() {
        // Given
        Mockito.when(projectDAO.getProjectById(CHI2050_ID)).thenReturn(Optional.of(createAscProjects().get(0)));
        Mockito.when(userDAO.getAllUsers()).thenReturn(this.createAscUsers());

        // When
        final ProjectResult prepareUpdateProject = currentResource.prepareUpdateProject(CHI2050_ID);

        // Then
        assertNotNull(prepareUpdateProject);
        assertNotNull(prepareUpdateProject.getUsers());
        assertNotNull(prepareUpdateProject.getUpdateProject());
    }

    @Test
    public void getProjectsByOwner_missingParameter() {
        // Given

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.getProjectsByOwner(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }
}
