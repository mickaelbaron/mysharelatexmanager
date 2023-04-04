package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BAD_USER_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_FIRSTNAME;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.INSTITUTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({ UserDAOMongo.class, SessionMongoTest.class })
public class UserDAOImplTest extends AbstractDAOTest {

	@Inject
	private IUserDAO currentDAO;

	@Test
	public void updateUserWithUnknownUserTest() {
		// Given
		final String filter = BARON_FIRSTNAME;
		List<UserEntity> allUsers = currentDAO.getAllUsers(filter);
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(1, allUsers.size());
		UserEntity currentUser = allUsers.get(0);
		// Bad ID.
		currentUser.setId(BAD_USER_ID);

		// When
		final boolean updateUser = currentDAO.updateUser(currentUser);

		// Then
		Assert.assertFalse(updateUser);
	}

	@Test
	public void updateUserTest() {
		// Given
		final String filter = BARON_FIRSTNAME;
		List<UserEntity> allUsers = currentDAO.getAllUsers(filter);
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(1, allUsers.size());
		UserEntity currentUser = allUsers.get(0);
		Assert.assertEquals(BARON_ID, currentUser.getId());
		currentUser.setFirstName(BARON_FIRSTNAME + BARON_FIRSTNAME);
		currentUser.setLastName(BARON + BARON);

		// When
		final boolean updateUser = currentDAO.updateUser(currentUser);

		// Then
		Assert.assertTrue(updateUser);
		allUsers = currentDAO.getAllUsers(filter);
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(1, allUsers.size());
		currentUser = allUsers.get(0);
		Assert.assertEquals(BARON_FIRSTNAME + BARON_FIRSTNAME, currentUser.getFirstName());
		Assert.assertEquals(BARON + BARON, currentUser.getLastName());
	}

	@Test
	public void getAllUsersTest() {
		// Given
		// Database initialization

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(new ArrayList<SortedData>());

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(3, allUsers.size());
		final UserEntity user = allUsers.get(0);
		Assert.assertEquals(BARON_ID, user.getId());
		Assert.assertEquals(BARON, user.getLastName());
		Assert.assertEquals(true, user.isAdmin());
		Assert.assertEquals(INSTITUTION, user.getInstitution());
		Assert.assertEquals("Mickael", user.getFirstName());
		Assert.assertEquals(BARON_EMAIL, user.getEmail());
		Assert.assertEquals((Integer) 120, user.getLoginCount());
		Assert.assertEquals("Wed Nov 15 23:02:50 CET 2017", user.getLastLoggedIn().toString());
	}

	@Test
	public void getAllUsersWithAscSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(LAST_NAME, true));

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(3, allUsers.size());
		Assert.assertEquals(BARON, allUsers.get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.get(1).getLastName());
		Assert.assertEquals(ZOUBIDA, allUsers.get(2).getLastName());
	}

	@Test
	public void getAllUsersWithMultipleAscSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(EMAIL, true));
		sorts.add(new SortedData(LAST_NAME, true));

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(3, allUsers.size());
		Assert.assertEquals(BARON, allUsers.get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.get(1).getLastName());
		Assert.assertEquals(ZOUBIDA, allUsers.get(2).getLastName());
	}

	@Test
	public void getAllUsersWithDescSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(LAST_NAME, false));

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(3, allUsers.size());
		Assert.assertEquals(ZOUBIDA, allUsers.get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.get(1).getLastName());
		Assert.assertEquals(BARON, allUsers.get(2).getLastName());
	}

	@Test
	public void getAllUsersWithMultipleDescSorterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(EMAIL, false));
		sorts.add(new SortedData(LAST_NAME, false));

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(3, allUsers.size());
		Assert.assertEquals(BARON, allUsers.get(2).getLastName());
		Assert.assertEquals(POILU, allUsers.get(1).getLastName());
		Assert.assertEquals(ZOUBIDA, allUsers.get(0).getLastName());
	}

	@Test
	public void getAllUsersWithFilterTest() {
		// Given
		final String filter = "BARON";

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(filter);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(1, allUsers.size());
		Assert.assertEquals(BARON_EMAIL, allUsers.get(0).getEmail());
	}

	@Test
	public void getAllUsersWithFilterNoResultTest() {
		// Given
		final String filter = "blabla";

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(filter);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(0, allUsers.size());
	}

	@Test
	public void getAllUsersWithAscSorterAndFilterTest() {
		// Given
		// Database initialization.
		List<SortedData> sorts = new ArrayList<SortedData>();
		sorts.add(new SortedData(LAST_NAME, true));
		final String filter = INSTITUTION;

		// When
		final List<UserEntity> allUsers = currentDAO.getAllUsers(sorts, filter);

		// Then
		Assert.assertNotNull(allUsers);
		Assert.assertEquals(2, allUsers.size());
		Assert.assertEquals(BARON, allUsers.get(0).getLastName());
		Assert.assertEquals(POILU, allUsers.get(1).getLastName());
	}

	@Test
	public void getUserByIdTest() {
		// Given

		// When
		final Optional<UserEntity> userById = currentDAO.getUserById(BARON_ID);

		// Then
		Assert.assertTrue(userById.isPresent());
		Assert.assertEquals(BARON, userById.get().getLastName());
	}

	@Test
	public void getUserByIdWithNoObjedctIdTest() {
		// Given

		// When
		final Optional<UserEntity> userById = currentDAO.getUserById("1");

		// Then
		Assert.assertFalse(userById.isPresent());
	}

	@Test
	public void getUserByIdWithBadIdTest() {
		// Given

		// When
		final Optional<UserEntity> userById = currentDAO.getUserById(BAD_USER_ID);

		// Then
		Assert.assertTrue(!userById.isPresent());
	}

	@Test
	public void deleteUserTest() {
		// Given

		// When
		final boolean deleteUser = currentDAO.deleteUser(BARON_ID);

		// Then
		Assert.assertTrue(deleteUser);
		final Optional<UserEntity> userById = currentDAO.getUserById(BARON_ID);
		Assert.assertFalse(userById.isPresent());
	}

	@Test
	public void deleteUserWithBadIdTest() {
		// Given

		// When
		final boolean deleteUser = currentDAO.deleteUser(BAD_USER_ID);

		// Then
		Assert.assertFalse(deleteUser);
	}

	@Test
	public void deleteUserWithNoObjectIdTest() {
		// Given

		// When
		final boolean deleteUser = currentDAO.deleteUser("1");

		// Then
		Assert.assertFalse(deleteUser);
	}
}
