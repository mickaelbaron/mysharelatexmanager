package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.AUTHENTICATION;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.LOGIN;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.LOGOUT;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.mickaelbaron.mysharelatexmanager.model.Credentials;
import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;

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
