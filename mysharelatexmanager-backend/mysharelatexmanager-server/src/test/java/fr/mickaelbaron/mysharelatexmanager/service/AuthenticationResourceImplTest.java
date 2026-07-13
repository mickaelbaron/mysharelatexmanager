package fr.mickaelbaron.mysharelatexmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;
import fr.mickaelbaron.mysharelatexmanager.model.Credentials;
import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationResourceImplTest extends AbstractServiceTest {

    @InjectMocks
    private AuthenticationResourceImpl currentAuthentication;

    @Produces
    @Mock
    IConfigExecution config;

    @Test
    public void login_missingLoginValue() {
        // Given
        Credentials newCredentials = new Credentials();
        newCredentials.setPassword("adminadmin");

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(newCredentials));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_missingPasswordValue() {
        // Given
        Credentials newCredentials = new Credentials();
        newCredentials.setUsername("admin");

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(newCredentials));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_missingBothLoginAndPasswordValues() {
        // Given
        Credentials newCredentials = new Credentials();

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(newCredentials));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_missingParameter() {
        // Given

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(null));

        // Then
        assertEquals("Parameter is missing.", exception.getMessage());
        assertEquals(Status.BAD_REQUEST, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_invalidLoginValue() {
        // Given
        Credentials newCredentials = new Credentials();
        newCredentials.setUsername("bad");
        newCredentials.setPassword("adminadmin");
        when(config.getIdentificationUser()).thenReturn("admin");

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(newCredentials));

        // Then
        assertEquals("Invalid Login.", exception.getMessage());
        assertEquals(Status.UNAUTHORIZED, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_invalidPasswordValue() {
        // Given
        Credentials newCredentials = new Credentials();
        newCredentials.setUsername("admin");
        newCredentials.setPassword("bad");
        when(config.getIdentificationUser()).thenReturn("admin");
        when(config.getIdentificationPassword()).thenReturn("adminadmin");

        // When
        WebApplicationException exception = assertThrows(
                WebApplicationException.class,
                () -> currentAuthentication.login(newCredentials));

        // Then
        assertEquals("Invalid Password.", exception.getMessage());
        assertEquals(Status.UNAUTHORIZED, exception.getResponse().getStatusInfo());
    }

    @Test
    public void login_success() {
        // Given
        Credentials newCredentials = new Credentials();
        newCredentials.setUsername("admin");
        newCredentials.setPassword("adminadmin");

        when(config.getIdentificationUser()).thenReturn("admin");
        when(config.getIdentificationPassword()).thenReturn("adminadmin");
        when(config.getEncryptPassword()).thenReturn("!thisismypassword!");
        when(config.getEncryptNoise()).thenReturn("@BCDEFGHIJKLMNPQRSTUVWXZ&bcdefghijklmnopqrstuvwxyz0123456789");

        // When
        final CredentialsResult login = currentAuthentication.login(newCredentials);

        // Then
        assertNotNull(login);
        assertNotNull(login.getToken());
        assertEquals("admin", login.getUsername());
        assertEquals(192, login.getToken().length());
    }

}
