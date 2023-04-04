package fr.mickaelbaron.mysharelatexmanager;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.mickaelbaron.mysharelatexmanager.dao.AllDAOTests;
import fr.mickaelbaron.mysharelatexmanager.service.AllServiceTests;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(Suite.class)
@SuiteClasses(value = { AllServiceTests.class, AllDAOTests.class })
public class AllTests {

}