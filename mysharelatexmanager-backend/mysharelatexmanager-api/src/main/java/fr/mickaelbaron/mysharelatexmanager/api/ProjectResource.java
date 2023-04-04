package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.FILTER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.ID;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiParameters.SORT;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.OWNER;
import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.PROJECTS;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.mickaelbaron.mysharelatexmanager.model.Project;
import fr.mickaelbaron.mysharelatexmanager.model.ProjectResult;

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
