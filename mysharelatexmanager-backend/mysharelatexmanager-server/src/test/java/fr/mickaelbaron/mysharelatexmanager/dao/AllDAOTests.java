package fr.mickaelbaron.mysharelatexmanager.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.mickaelbaron.mysharelatexmanager.dao.mongo.ProjectDAOImplTest;
import fr.mickaelbaron.mysharelatexmanager.dao.mongo.UserDAOImplTest;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(Suite.class)
@SuiteClasses(value = { UserDAOImplTest.class, ProjectDAOImplTest.class })
public class AllDAOTests {

}