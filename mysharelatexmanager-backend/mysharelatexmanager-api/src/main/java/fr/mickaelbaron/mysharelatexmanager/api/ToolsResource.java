package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.TOOLS;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.mickaelbaron.mysharelatexmanager.model.ToolsResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path(TOOLS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ToolsResource {

	@POST
	@TokenAuthenticated
	@Path(ApiPaths.USER_FILES)
	ToolsResult cleanUserFilesDirectory();
}
