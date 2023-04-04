package fr.mickaelbaron.mysharelatexmanager.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;
import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.ShortUser;
import fr.mickaelbaron.mysharelatexmanager.model.User;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class EntityFactory {

	private EntityFactory() {
		throw new IllegalStateException("Utility class");
	}
	
	public static CredentialsResult createCredentialsResult(String username, String token) {
		CredentialsResult newCredentialsResult = new CredentialsResult();
		newCredentialsResult.setToken(token);
		newCredentialsResult.setUsername(username);
		
		return newCredentialsResult;
	}

	public static Project createProject(ProjectEntity refProject) {
		Project newProject = new Project();
		newProject.setActive(refProject.isActive());
		newProject.setCollaberators(createShortUsers(refProject.getCollaberators()));
		newProject.setDescription(refProject.getDescription());
		newProject.setId(refProject.getId());
		newProject.setName(refProject.getName());
		newProject.setOwner(refProject.getOwner());
		newProject.setOwnerId(refProject.getOwnerId());
		newProject.setLastUpdated(refProject.getLastUpdated());

		return newProject;
	}

	public static List<ShortUser> createShortUsers(List<ShortUserEntity> refShortUserEntities) {
		List<ShortUser> shortUsers = new ArrayList<>();
		
		if (refShortUserEntities != null) {
			for(ShortUserEntity refShortUserEntity : refShortUserEntities) {
				ShortUser newShortUser = new ShortUser();
				
				newShortUser.setEmail(refShortUserEntity.getEmail());
				newShortUser.setId(refShortUserEntity.getId());
				shortUsers.add(newShortUser);
			}			
		}
		
		return shortUsers;
	}
	
	public static List<String> createCollaborators(List<ShortUser> refShortUsers) {
		return refShortUsers.stream().map(t -> t.getId()).collect(Collectors.toList());
		
	}
	
	public static List<ShortUserEntity> createShortUserEntities(List<ShortUser> refShortUsers) {
		List<ShortUserEntity> shortUserEntities = new ArrayList<>();
		
		for(ShortUser refShortUser : refShortUsers) {
			ShortUserEntity newShortUserEntity = new ShortUserEntity();
			
			newShortUserEntity.setEmail(refShortUser.getEmail());
			newShortUserEntity.setId(refShortUser.getId());
			shortUserEntities.add(newShortUserEntity);
		}
		
		return shortUserEntities;
	}
	
	public static ProjectEntity createProjectEntity(Project refProject) {
		ProjectEntity newProject = new ProjectEntity();
		newProject.setActive(refProject.isActive());
		newProject.setCollaberatorsId(createCollaborators(refProject.getCollaberators()));
		newProject.setDescription(refProject.getDescription());
		newProject.setId(refProject.getId());
		newProject.setName(refProject.getName());
		newProject.setOwner(refProject.getOwner());
		newProject.setOwnerId(refProject.getOwnerId());
		newProject.setLastUpdated(refProject.getLastUpdated());
		
		return newProject;
	}

	public static List<Project> createProjects(List<ProjectEntity> refProjects) {
		if (refProjects != null) {
			List<Project> newProjects = new ArrayList<>();
			for (ProjectEntity current : refProjects) {
				newProjects.add(EntityFactory.createProject(current));
			}
			return newProjects;
		} else {
			return null;
		}
	}

	public static UserEntity createUserEntity(User refUser) {
		UserEntity newUser = new UserEntity();
		newUser.setAdmin(refUser.isAdmin());
		newUser.setEmail(refUser.getEmail());
		newUser.setFirstName(refUser.getFirstName());
		newUser.setHashedPassword(refUser.getHashedPassword());
		newUser.setId(refUser.getId());
		newUser.setInstitution(refUser.getInstitution());
		newUser.setLastLoggedIn(refUser.getLastLoggedIn());
		newUser.setLastName(refUser.getLastName());
		newUser.setLoginCount(refUser.getLoginCount());
		return newUser;
	}

	public static User createUser(UserEntity refUser) {
		User newUser = new User();
		newUser.setAdmin(refUser.isAdmin());
		newUser.setEmail(refUser.getEmail());
		newUser.setFirstName(refUser.getFirstName());
		newUser.setHashedPassword(refUser.getHashedPassword());
		newUser.setId(refUser.getId());
		newUser.setInstitution(refUser.getInstitution());
		newUser.setLastLoggedIn(refUser.getLastLoggedIn());
		newUser.setLastName(refUser.getLastName());
		newUser.setLoginCount(refUser.getLoginCount());
		return newUser;
	}

	public static List<User> createUsers(List<UserEntity> refUsers) {
		if (refUsers != null) {
			List<User> newUsers = new ArrayList<>();
			for (UserEntity current : refUsers) {
				newUsers.add(EntityFactory.createUser(current));
			}
			return newUsers;
		} else {
			return null;
		}
	}
}
