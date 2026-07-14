package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.FILTER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.ID;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.SORT;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.USERS;

import fr.mickaelbaron.mysharelatexmanager.model.TransferUserProjects;
import fr.mickaelbaron.mysharelatexmanager.model.User;
import fr.mickaelbaron.mysharelatexmanager.model.UserResult;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
	@Path(ApiPaths.TRANSFER)
	UserResult transferUserProjects(TransferUserProjects transferUserProjects);

	@GET
	@TokenAuthenticated
	@Path(ApiPaths.TRANSFER + "/{" + ID + "}")
	UserResult prepareTransferUserProjects(@PathParam(ID) String id);

	@DELETE
	@TokenAuthenticated
	@Path(ApiPaths.COLLABORATOR + "/{" + ID + "}")
	UserResult removeCollaborator(@PathParam(ID) String id);
}
