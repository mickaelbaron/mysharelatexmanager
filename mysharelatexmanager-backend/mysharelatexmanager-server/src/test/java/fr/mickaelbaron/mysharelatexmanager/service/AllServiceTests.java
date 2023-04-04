package fr.mickaelbaron.mysharelatexmanager.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(Suite.class)
@SuiteClasses(value = { UserResourceImplTest.class, ProjectResourceImplTest.class,
		AuthenticationResourceImplTest.class })
public class AllServiceTests {

}