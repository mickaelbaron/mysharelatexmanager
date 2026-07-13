package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BAD_USER_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_FIRSTNAME;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.INSTITUTION;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_NAME;
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

import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;

@ExtendWith(MockitoExtension.class)
public class UserDAOMongoTest extends AbstractDAOTest {

    @InjectMocks
    private UserDAOMongo currentDAO;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Inject specific implementation of SessionMongoTest in currentDAO.
        currentDAO.refSessionMongo = this.refSessionMongo;
    }

    @Test
    public void updateUser_success() {
        // Given
        final String filter = BARON_FIRSTNAME;
        List<UserEntity> allUsers = currentDAO.getAllUsers(filter);
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        UserEntity currentUser = allUsers.get(0);
        assertEquals(BARON_ID, currentUser.getId());
        currentUser.setFirstName(BARON_FIRSTNAME + BARON_FIRSTNAME);
        currentUser.setLastName(BARON + BARON);

        // When
        final boolean updateUser = currentDAO.updateUser(currentUser);

        // Then
        assertTrue(updateUser);
        allUsers = currentDAO.getAllUsers(filter);
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        currentUser = allUsers.get(0);
        assertEquals(BARON_FIRSTNAME + BARON_FIRSTNAME, currentUser.getFirstName());
        assertEquals(BARON + BARON, currentUser.getLastName());
    }

    @Test
    public void updateUser_withUnknownUser() {
        // Given
        final String filter = BARON_FIRSTNAME;
        List<UserEntity> allUsers = currentDAO.getAllUsers(filter);
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        UserEntity currentUser = allUsers.get(0);
        // Bad ID.
        currentUser.setId(BAD_USER_ID);

        // When
        final boolean updateUser = currentDAO.updateUser(currentUser);

        // Then
        assertFalse(updateUser);
    }

    @Test
    public void getAllUsers_success() {
        // Given
        // Database initialization

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(new ArrayList<SortedData>());

        // Then
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        final UserEntity user = allUsers.get(0);
        assertEquals(BARON_ID, user.getId());
        assertEquals(BARON, user.getLastName());
        assertEquals(true, user.isAdmin());
        assertEquals(INSTITUTION, user.getInstitution());
        assertEquals("Mickael", user.getFirstName());
        assertEquals(BARON_EMAIL, user.getEmail());
        assertEquals((Integer) 120, user.getLoginCount());
        assertEquals("Wed Nov 15 23:02:50 CET 2017", user.getLastLoggedIn().toString());
    }

    @Test
    public void getAllUsers_withAscSorter() {
        // Given
        // Database initialization.
        List<SortedData> sorts = new ArrayList<SortedData>();
        sorts.add(new SortedData(LAST_NAME, true));

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

        // Then
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        assertEquals(BARON, allUsers.get(0).getLastName());
        assertEquals(POILU, allUsers.get(1).getLastName());
        assertEquals(ZOUBIDA, allUsers.get(2).getLastName());
    }

    @Test
    public void getAllUsers_withMultipleAscSorter() {
        // Given
        // Database initialization.
        List<SortedData> sorts = new ArrayList<SortedData>();
        sorts.add(new SortedData(EMAIL, true));
        sorts.add(new SortedData(LAST_NAME, true));

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

        // Then
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        assertEquals(BARON, allUsers.get(0).getLastName());
        assertEquals(POILU, allUsers.get(1).getLastName());
        assertEquals(ZOUBIDA, allUsers.get(2).getLastName());
    }

    @Test
    public void getAllUsers_withDescSorter() {
        // Given
        // Database initialization.
        List<SortedData> sorts = new ArrayList<SortedData>();
        sorts.add(new SortedData(LAST_NAME, false));

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

        // Then
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        assertEquals(ZOUBIDA, allUsers.get(0).getLastName());
        assertEquals(POILU, allUsers.get(1).getLastName());
        assertEquals(BARON, allUsers.get(2).getLastName());
    }

    @Test
    public void getAllUsers_withMultipleDescSorter() {
        // Given
        // Database initialization.
        List<SortedData> sorts = new ArrayList<SortedData>();
        sorts.add(new SortedData(EMAIL, false));
        sorts.add(new SortedData(LAST_NAME, false));

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

        // Then
        assertNotNull(allUsers);
        assertEquals(3, allUsers.size());
        assertEquals(BARON, allUsers.get(2).getLastName());
        assertEquals(POILU, allUsers.get(1).getLastName());
        assertEquals(ZOUBIDA, allUsers.get(0).getLastName());
    }

    @Test
    public void getAllUsers_withFilter() {
        // Given
        final String filter = "BARON";

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(filter);

        // Then
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        assertEquals(BARON_EMAIL, allUsers.get(0).getEmail());
    }

    @Test
    public void getAllUsers_withFilterNoResult() {
        // Given
        final String filter = "blabla";

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(filter);

        // Then
        assertNotNull(allUsers);
        assertEquals(0, allUsers.size());
    }

    @Test
    public void getAllUsers_withAscSorterAndFilter() {
        // Given
        // Database initialization.
        List<SortedData> sorts = new ArrayList<SortedData>();
        sorts.add(new SortedData(LAST_NAME, true));
        final String filter = INSTITUTION;

        // When
        final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts, filter);

        // Then
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        assertEquals(BARON, allUsers.get(0).getLastName());
        assertEquals(POILU, allUsers.get(1).getLastName());
    }

    @Test
    public void getUserById_success() {
        // Given

        // When
        final Optional<UserEntity> userById = currentDAO.getUserById(BARON_ID);

        // Then
        assertTrue(userById.isPresent());
        assertEquals(BARON, userById.get().getLastName());
    }

    @Test
    public void getUserById_withNoObjedctId() {
        // Given

        // When
        final Optional<UserEntity> userById = currentDAO.getUserById("1");

        // Then
        assertFalse(userById.isPresent());
    }

    @Test
    public void getUserById_withBadId() {
        // Given

        // When
        final Optional<UserEntity> userById = currentDAO.getUserById(BAD_USER_ID);

        // Then
        assertTrue(!userById.isPresent());
    }

    @Test
    public void deleteUser_success() {
        // Given

        // When
        final boolean deleteUser = currentDAO.deleteUser(BARON_ID);

        // Then
        assertTrue(deleteUser);
        final Optional<UserEntity> userById = currentDAO.getUserById(BARON_ID);
        assertFalse(userById.isPresent());
    }

    @Test
    public void deleteUser_withBadId() {
        // Given

        // When
        final boolean deleteUser = currentDAO.deleteUser(BAD_USER_ID);

        // Then
        assertFalse(deleteUser);
    }

    @Test
    public void deleteUser_withNoObjectId() {
        // Given

        // When
        final boolean deleteUser = currentDAO.deleteUser("1");

        // Then
        assertFalse(deleteUser);
    }
}
