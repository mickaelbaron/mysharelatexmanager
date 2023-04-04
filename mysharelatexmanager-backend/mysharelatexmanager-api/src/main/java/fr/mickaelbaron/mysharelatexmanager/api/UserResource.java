package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.FILTER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.ID;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.SORT;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.USERS;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.mickaelbaron.mysharelatexmanager.model.TransfertUserProjects;
import fr.mickaelbaron.mysharelatexmanager.model.User;
import fr.mickaelbaron.mysharelatexmanager.model.UserResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path(USERS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserResource {

	@GET
	@TokenAuthenticated
	UserResult getAllUsers(@DefaultValue("") @QueryParam(SORT) String sorted,
			@DefaultValue("") @QueryParam(FILTER) String filter);

	@PUT
	@TokenAuthenticated
	Response updateUser(User newUser);

	@DELETE
	@TokenAuthenticated
	@Path("{" + ID + "}")
	UserResult deleteUser(@PathParam(ID) String id);

	@PUT
	@TokenAuthenticated
	@Path(ApiPaths.TRANSFERT)
	UserResult transfertUserProjects(TransfertUserProjects transfertUserProjects);

	@GET
	@TokenAuthenticated
	@Path(ApiPaths.TRANSFERT + "/{" + ID + "}")
	UserResult prepareTransfertUserProjects(@PathParam(ID) String id);

	@DELETE
	@TokenAuthenticated
	@Path(ApiPaths.COLLABERATOR + "/{" + ID + "}")
	UserResult removeCollaberator(@PathParam(ID) String id);
}
