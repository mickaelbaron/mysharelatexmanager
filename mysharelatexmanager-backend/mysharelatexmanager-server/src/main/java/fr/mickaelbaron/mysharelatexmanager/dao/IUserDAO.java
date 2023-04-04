package fr.mickaelbaron.mysharelatexmanager.dao;

import java.util.List;
import java.util.Optional;

import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public interface IUserDAO {

	/**
	 * @return
	 */
	List<UserEntity> getAllUsers(List<SortedData> sorted, String filter);

	/**
	 * @param sorted
	 * @return
	 */
	List<UserEntity> getAllUsers(List<SortedData> sorted);

	/**
	 * @param filter
	 * @return
	 */
	List<UserEntity> getAllUsers(String filter);

	/**
	 * @return
	 */
	List<UserEntity> getAllUsers();

	/**
	 * @param newValue
	 */
	boolean updateUser(UserEntity updateValue);

	/**
	 * @param id
	 * @return
	 */
	Optional<UserEntity> getUserById(String id);

	/**
	 * @param id
	 * @return
	 */
	boolean deleteUser(String id);
}
