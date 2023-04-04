package fr.mickaelbaron.mysharelatexmanager.service;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.BARON_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.CHI2050_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.POILU_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.SUPER_CONF_ID;
import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerTestConstant.ZOUBIDA_ID;

import java.util.ArrayList;
import java.util.List;

import fr.mickaelbaron.mysharelatexmanager.AbstractCDIUnitTest;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AbstractResourceTest extends AbstractCDIUnitTest {

	protected List<UserEntity> createAscUsers() {
		List<UserEntity> users = new ArrayList<UserEntity>();
		UserEntity newUser = new UserEntity();
		newUser.setId(BARON_ID);
		newUser.setLastName(BARON);
		users.add(newUser);
		newUser = new UserEntity();
		newUser.setId(POILU_ID);
		newUser.setLastName(POILU);
		users.add(newUser);

		return users;
	}

	protected List<UserEntity> createDescUsers() {
		List<UserEntity> users = new ArrayList<UserEntity>();
		UserEntity newUser = new UserEntity();
		newUser.setId(POILU_ID);
		newUser.setLastName(POILU);
		users.add(newUser);
		newUser = new UserEntity();
		newUser.setId(BARON_ID);
		newUser.setLastName(BARON);
		users.add(newUser);

		return users;
	}

	protected List<ProjectEntity> createAscProjects() {
		List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
		ProjectEntity newProject = new ProjectEntity();
		newProject.setId(CHI2050_ID);
		newProject.setName(CHI2050);
		projects.add(newProject);
		newProject = new ProjectEntity();
		newProject.setId(SUPER_CONF_ID);
		newProject.setName(SUPER_CONF);
		projects.add(newProject);

		return projects;
	}

	protected List<ProjectEntity> createDescProjects() {
		List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
		ProjectEntity newProject = new ProjectEntity();
		newProject.setId(SUPER_CONF_ID);
		newProject.setName(SUPER_CONF);
		projects.add(newProject);
		newProject = new ProjectEntity();
		newProject.setId(CHI2050_ID);
		newProject.setName(CHI2050);
		projects.add(newProject);

		return projects;
	}
	
	protected List<ProjectEntity> createProjects() {
		List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
		ProjectEntity newProject = new ProjectEntity();
		newProject.setId(CHI2050_ID);
		newProject.setOwnerId(BARON_ID);
		List<String> collaberatorsId = new ArrayList<String>();
		collaberatorsId.add(POILU_ID);
		collaberatorsId.add(ZOUBIDA_ID);
		newProject.setCollaberatorsId(collaberatorsId);
		projects.add(newProject);
		
		return projects;
	}
}
