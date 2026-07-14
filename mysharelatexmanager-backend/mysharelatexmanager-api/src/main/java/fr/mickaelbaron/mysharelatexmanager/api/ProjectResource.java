package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.FILTER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.ID;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.SORT;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.OWNER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.PROJECTS;

import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.ProjectResult;
import jakarta.ws.rs.Consumes;
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
@Path(PROJECTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectResource {

	@TokenAuthenticated
	@GET
	ProjectResult getAllProjects(@DefaultValue("") @QueryParam(SORT) String sorted,
			@DefaultValue("") @QueryParam(FILTER) String filter);

	@TokenAuthenticated
	@PUT
	Response updateProject(Project newProject);

	@TokenAuthenticated
	@GET
	@Path(OWNER + "/{" + ID + "}")
	ProjectResult getProjectsByOwner(@PathParam(ID) String id);

	@TokenAuthenticated
	@GET
	@Path("{" + ID + "}")
	ProjectResult prepareUpdateProject(@PathParam(ID) String id);
}
