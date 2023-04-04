package fr.mickaelbaron.mysharelatexmanager.service;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;
import fr.mickaelbaron.mysharelatexmanager.model.Credentials;
import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RunWith(CdiRunner.class)
public class AuthenticationResourceImplTest extends AbstractResourceTest {

	@Inject
	private AuthenticationResourceImpl currentAuthentication;

	@Produces
	@Mock
	IConfigExecution config;
	
	@Test
	public void loginWithoutUsernameTest() {
		// Given
		Credentials newCredentials = new Credentials();
		newCredentials.setPassword("adminadmin");

		try {
			// When
			currentAuthentication.login(newCredentials);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void loginWithoutPasswordTest() {
		// Given
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername("admin");

		try {
			// When
			currentAuthentication.login(newCredentials);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void loginWithoutPasswordAndUsernameTest() {
		// Given
		Credentials newCredentials = new Credentials();

		try {
			// When
			currentAuthentication.login(newCredentials);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Parameter is missing.", e.getMessage());
			Assert.assertEquals(Status.BAD_REQUEST, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void loginWithBadUsernameTest() {
		// Given
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername("bad");
		newCredentials.setPassword("adminadmin");
		Mockito.when(config.getIdentificationUser()).thenReturn("admin");
		
		try {
			// When
			currentAuthentication.login(newCredentials);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Invalid Login.", e.getResponse().getEntity());
			Assert.assertEquals(Status.UNAUTHORIZED, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void loginWithBadPasswordTest() {
		// Given
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername("admin");
		newCredentials.setPassword("adminadminbad");
		Mockito.when(config.getIdentificationUser()).thenReturn("admin");
		Mockito.when(config.getIdentificationPassword()).thenReturn("adminadmin");
		
		try {
			// When
			currentAuthentication.login(newCredentials);
			Assert.fail("Must throw a WebApplicationException");
		} catch (WebApplicationException e) {
			// Then
			Assert.assertEquals("Invalid Password.", e.getResponse().getEntity());
			Assert.assertEquals(Status.UNAUTHORIZED, e.getResponse().getStatusInfo());
		}
	}

	@Test
	public void loginTest() {
		// Given
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername("admin");
		newCredentials.setPassword("adminadmin");
		Mockito.when(config.getIdentificationUser()).thenReturn("admin");
		Mockito.when(config.getIdentificationPassword()).thenReturn("adminadmin");
		Mockito.when(config.getEncryptPassword()).thenReturn("!thisismypassword!");
		Mockito.when(config.getEncryptNoise()).thenReturn("@BCDEFGHIJKLMNPQRSTUVWXZ&bcdefghijklmnopqrstuvwxyz0123456789");
		
		// When
		final CredentialsResult login = currentAuthentication.login(newCredentials);

		// Then
		Assert.assertNotNull(login);
		Assert.assertNotNull(login.getToken());
		Assert.assertEquals("admin", login.getUsername());
		Assert.assertEquals(192, login.getToken().length());
	}

}
