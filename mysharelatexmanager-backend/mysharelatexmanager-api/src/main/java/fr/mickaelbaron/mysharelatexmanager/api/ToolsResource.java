package fr.mickaelbaron.mysharelatexmanager.api;

import static fr.mickaelbaron.mysharelatexmanager.api.ApiPaths.TOOLS;

import fr.mickaelbaron.mysharelatexmanager.model.ToolsResult;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
