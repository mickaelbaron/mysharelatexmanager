package fr.mickaelbaron.mysharelatexmanager;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AbstractCDIUnitTest {

	@After
	public void tearDown() throws NamingException {
		InitialContext initialContext = new InitialContext();
		initialContext.unbind("java:comp/BeanManager");
	}
}
