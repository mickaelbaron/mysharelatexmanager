package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.AUTHENTICATION;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.LOGIN;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.LOGOUT;

import fr.mickaelbaron.mysharelatexmanager.model.Credentials;
import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path(AUTHENTICATION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthenticationResource {

	@Path(LOGIN)
	@POST
	CredentialsResult login(Credentials credentials);

	@Path(LOGOUT)
	@DELETE
	Response logout();
}
