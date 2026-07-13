package fr.mickaelbaron.mysharelatexmanager.service;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BAD_USER_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA_ID;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;
import fr.mickaelbaron.mysharelatexmanager.model.TransferUserProjects;
import fr.mickaelbaron.mysharelatexmanager.model.TransferUserProjects.MODE_ENUM;
import fr.mickaelbaron.mysharelatexmanager.model.UserResult;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ExtendWith(MockitoExtension.class)
public class UserResourceImplTest extends AbstractServiceTest {

    @InjectMocks
    private UserResourceImpl currentResource;

    @Produces
    @Mock
    IUserDAO userDAO;

    @Produces
    @Mock
    IProjectDAO projectDAO;

    @Test
    public void getAllUsers_sucess() {
        // Given
        when(userDAO.getAllUsers(new ArrayList<SortedData>(), "")).thenReturn(createAscUsers());

        // When
        final UserResult allUsers = currentResource.getAllUsers("", "");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(BARON_ID, allUsers.getData().get(0).getId());
        assertEquals(POILU_ID, allUsers.getData().get(1).getId());
    }

    @Test
    public void getAllUsers_withAscSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(LAST_NAME, true));

        Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createAscUsers());

        // When
        String sorted = LAST_NAME + "|asc";
        final UserResult allUsers = currentResource.getAllUsers(sorted, "");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(BARON, allUsers.getData().get(0).getLastName());
        assertEquals(POILU, allUsers.getData().get(1).getLastName());
    }

    @Test
    public void getAllUsers_withMultipleAscSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(EMAIL, true));
        sortedData.add(new SortedData(LAST_NAME, true));

        Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createAscUsers());

        // When
        String sorted = EMAIL + "|asc," + LAST_NAME + "|asc";
        final UserResult allUsers = currentResource.getAllUsers(sorted, "");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(BARON, allUsers.getData().get(0).getLastName());
        assertEquals(POILU, allUsers.getData().get(1).getLastName());
    }

    @Test
    public void getAllUsers_withDescSorter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(LAST_NAME, false));

        Mockito.when(userDAO.getAllUsers(sortedData, "")).thenReturn(createDescUsers());

        // When
        String sorted = LAST_NAME + "|desc";
        final UserResult allUsers = currentResource.getAllUsers(sorted, "");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(POILU, allUsers.getData().get(0).getLastName());
        assertEquals(BARON, allUsers.getData().get(1).getLastName());
    }

    @Test
    public void getAllUsers_withAscSorterAndFilter() {
        // Given
        List<SortedData> sortedData = new ArrayList<SortedData>();
        sortedData.add(new SortedData(LAST_NAME, true));

        Mockito.when(userDAO.getAllUsers(sortedData, "baron")).thenReturn(createAscUsers());

        // When
        String sorted = LAST_NAME + "|asc";
        final UserResult allUsers = currentResource.getAllUsers(sorted, "baron");

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(BARON, allUsers.getData().get(0).getLastName());
        assertEquals(POILU, allUsers.getData().get(1).getLastName());
    }

    @Test
    public void getAllUsers_withNullParametersAndNoProjects() {
        // Given
        Mockito.when(userDAO.getAllUsers(new ArrayList<SortedData>(), "")).thenReturn(null);

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.getAllUsers(null, null));

        // Then
        assertEquals("Something is wrong.", exception.getMessage());
        assertEquals(Status.INTERNAL_SERVER_ERROR, exception.getResponse().getStatusInfo());
    }

    @Test
    public void getAllUsers_withNullParameters() {
        // Given
        Mockito.when(userDAO.getAllUsers(new ArrayList<SortedData>(), "")).thenReturn(createAscUsers());

        // When
        final UserResult allUsers = currentResource.getAllUsers(null, null);

        // Then
        assertNotNull(allUsers);
        assertEquals((Integer) 2, allUsers.getTotal());
        assertEquals(BARON_ID, allUsers.getData().get(0).getId());
        assertEquals(POILU_ID, allUsers.getData().get(1).getId());
    }

    @Test
    public void deleteUser_success() {
        // Given
        Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
        Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
        Mockito.when(projectDAO.getAllProjectsByCollaboratorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
        Mockito.when(userDAO.deleteUser(BARON_ID)).thenReturn(true);

        // When@
        final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

        // Then
        assertEquals(0, deleteUser.getCollaboratorsProject().size());
        assertEquals(0, deleteUser.getOwnedProjects().size());
    }

    @Test
    public void deleteUser_withProblem() {
        // Given
        Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
        Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
        Mockito.when(projectDAO.getAllProjectsByCollaboratorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
        Mockito.when(userDAO.deleteUser(BARON_ID)).thenReturn(false);

        // When and Then
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.deleteUser(BARON_ID));

        // Then
        assertEquals("Cannot delete user: " + BARON_ID, exception.getMessage());
        assertEquals(Status.NOT_FOUND, exception.getResponse().getStatusInfo());
    }

    @Test
    public void deleteUser_withNullParameters() {
        // Given

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.deleteUser(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void deleteUser_withBadUserID() {
        // Given
        Mockito.when(userDAO.getUserById(BAD_USER_ID))
                .thenReturn(Optional.empty());

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.deleteUser(BAD_USER_ID));

        // Then
        assertEquals("User not found.", exception.getMessage());
        assertEquals(Status.NOT_FOUND, exception.getResponse().getStatusInfo());
    }

    @Test
    public void deleteUser_withExistingOwnerProjects() {
        // Given
        Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
        final List<ProjectEntity> createAscProjects = this.createAscProjects();
        Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(createAscProjects);
        Mockito.when(projectDAO.getAllProjectsByCollaboratorsId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());

        // When
        final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

        // Then
        assertEquals(2, deleteUser.getOwnedProjects().size());
        assertEquals(0, deleteUser.getCollaboratorsProject().size());
    }

    @Test
    public void deleteUser_withExistingCollaboratorsProjects() {
        // Given
        Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
        final List<ProjectEntity> createAscProjects = this.createAscProjects();
        Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(new ArrayList<ProjectEntity>());
        Mockito.when(projectDAO.getAllProjectsByCollaboratorsId(BARON_ID)).thenReturn(createAscProjects);

        // When
        final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

        // Then
        assertEquals(0, deleteUser.getOwnedProjects().size());
        assertEquals(2, deleteUser.getCollaboratorsProject().size());
    }

    @Test
    public void deleteUser_withExistingBothOwnerAndCollaboratorProjects() {
        // Given
        Mockito.when(userDAO.getUserById(BARON_ID)).thenReturn(Optional.of(this.createAscUsers().get(0)));
        final List<ProjectEntity> createAscProjects = this.createAscProjects();
        Mockito.when(projectDAO.getAllProjectsByOwnerId(BARON_ID)).thenReturn(createAscProjects);
        Mockito.when(projectDAO.getAllProjectsByCollaboratorsId(BARON_ID)).thenReturn(createAscProjects);

        // When
        final UserResult deleteUser = currentResource.deleteUser(BARON_ID);

        // Then
        assertEquals(2, deleteUser.getOwnedProjects().size());
        assertEquals(2, deleteUser.getCollaboratorsProject().size());
    }

    @Test
    public void transferUserProjects_withNullParameters() {
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.transferUserProjects(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void transferUserProjects_withoutCurrentOwnerId() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setMode(MODE_ENUM.ALL);
        newDTO.setCurrentOwnerId(null);

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.transferUserProjects(newDTO));

        // Then
        assertEquals("Parameter is missing: currentOwnerId", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void transfertUserProjects_withoutMode() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId(BARON_ID);
        newDTO.setNewOwnerId(ZOUBIDA_ID);
        newDTO.setMode(null);

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.transferUserProjects(newDTO));

        // Then
        assertEquals("Parameter is missing: mode", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void transfertUserProjects_withTheSameOwnerIds() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId(BARON_ID);
        newDTO.setNewOwnerId(BARON_ID);
        newDTO.setMode(MODE_ENUM.ALL);

        Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId()))
                .thenReturn(Optional.of(new UserEntity()));
        Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId()))
                .thenReturn(Optional.of(new UserEntity()));

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.transferUserProjects(newDTO));

        // Then
        assertEquals("OwnerId references must be different", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void transfertUserProjects_withoutNewOwnerId() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId("abcde");
        newDTO.setNewOwnerId(null);
        newDTO.setMode(MODE_ENUM.ALL);

        Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId()))
                .thenReturn(Optional.of(new UserEntity()));

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.transferUserProjects(newDTO));

        // Then
        assertEquals("Parameter is missing: newOwnerId", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void transfertUserProjects_allMode() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId(BARON_ID);
        newDTO.setNewOwnerId(ZOUBIDA_ID);
        newDTO.setMode(MODE_ENUM.ALL);

        Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(projectDAO.transferAllProjects(newDTO.getCurrentOwnerId(), newDTO.getNewOwnerId()))
                .thenReturn(new ArrayList<ProjectEntity>());

        // When
        final UserResult transfertUserProjects = currentResource.transferUserProjects(newDTO);

        // Then
        assertNotNull(transfertUserProjects);
        assertEquals(0, transfertUserProjects.getOwnedProjects().size());
    }

    @Test
    public void transfertUserProjects_forcedMode() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId(BARON_ID);
        newDTO.setNewOwnerId(ZOUBIDA_ID);
        newDTO.setMode(MODE_ENUM.FORCED);

        Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(
                projectDAO.transferProjectsIfExistingCollaborators(newDTO.getCurrentOwnerId(), newDTO.getNewOwnerId()))
                .thenReturn(new ArrayList<ProjectEntity>());

        // When
        final UserResult transfertUserProjects = currentResource.transferUserProjects(newDTO);

        // Then
        assertNotNull(transfertUserProjects);
        assertEquals(0, transfertUserProjects.getOwnedProjects().size());
    }

    @Test
    public void transfertUserProjects_normalMode() {
        // Given
        TransferUserProjects newDTO = new TransferUserProjects();
        newDTO.setCurrentOwnerId(BARON_ID);
        newDTO.setNewOwnerId(ZOUBIDA_ID);
        newDTO.setMode(MODE_ENUM.NORMAL);

        Mockito.when(userDAO.getUserById(newDTO.getCurrentOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(userDAO.getUserById(newDTO.getNewOwnerId())).thenReturn(Optional.of(new UserEntity()));
        Mockito.when(projectDAO.transferProjectsIfNewOwnerIsCollaborator(newDTO.getCurrentOwnerId(),
                newDTO.getNewOwnerId()))
                .thenReturn(new ArrayList<ProjectEntity>());

        // When
        final UserResult transferUserProjects = currentResource.transferUserProjects(newDTO);

        // Then
        assertNotNull(transferUserProjects);
        assertEquals(0, transferUserProjects.getOwnedProjects().size());
    }

    @Test
    public void prepareTransfertUserProjects_withNullParameters() {
        // Given
        String rid = null;

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentResource.prepareTransferUserProjects(rid));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }
}
