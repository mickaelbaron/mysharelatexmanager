package fr.mickaelbaron.mysharelatexmanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import fr.mickaelbaron.mysharelatexmanager.api.ToolsResource;
import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;
import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.model.ToolsResult;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class ToolsResourceImpl implements ToolsResource {

	@Inject
	IConfigExecution config;

	@Inject
	IProjectDAO refProjectDAO;

	private Set<String> getProjectIdsFromUserFilesDirectory(Path userFilesPath) throws IOException {
		Set<String> collectFromUserFilesDirectory = null;
		collectFromUserFilesDirectory = Files.list(userFilesPath)
				.filter(t -> t.getFileName().toString().length() == 49 && t.getFileName().toString().contains("_"))
				.map(t -> {
					String current = t.getFileName().toString();
					String[] split = current.split("_");
					return split[0];
				}).distinct().sorted().collect(Collectors.toSet());

		return collectFromUserFilesDirectory;
	}

	private Set<String> getProjectIdsFromDatabase() {
		List<ProjectEntity> allProjects = refProjectDAO.getAllProjects();

		return allProjects.stream().map(t -> t.getId()).sorted().collect(Collectors.toSet());
	}

	@Override
	public ToolsResult cleanUserFilesDirectory() {
		// Check if the user_files is existing, is a directory and is not empty.
		Path path = Paths.get(config.getUserFilesPath());

		if (!Files.exists(path)) {
			return new ToolsResult(true, "user_files directory is not configured.");
		}

		if (!Files.isDirectory(path)) {
			return new ToolsResult(true, "user_files is not a directory.");
		}

		try {
			if (Files.list(path).count() == 0) {
				return new ToolsResult(true, "user_files directory is empty.");
			}
		} catch (IOException e) {
			throw new WebApplicationException("Problem to check if user_files is a directory.",
					Status.INTERNAL_SERVER_ERROR);
		}

		// Get the project ids from the files.
		Set<String> collectFromUserFilesDirectory = null;
		try {
			collectFromUserFilesDirectory = getProjectIdsFromUserFilesDirectory(path);
		} catch (IOException e) {
			throw new WebApplicationException("Problem to retrieve all filename from the user_files directory.",
					Status.INTERNAL_SERVER_ERROR);
		}

		// Get the project ids.
		Set<String> collectFromMongo = getProjectIdsFromDatabase();

		// Keep the projects to be removed.
		collectFromUserFilesDirectory.removeAll(collectFromMongo);

		// Rename files.
		try {
			this.renameUserFiles(path, collectFromUserFilesDirectory);
		} catch (IOException e) {
			throw new WebApplicationException("Problem to retrieve the filename from the user_files directory",
					Status.INTERNAL_SERVER_ERROR);
		}

		ToolsResult newToolsResult = new ToolsResult();
		newToolsResult.setError(false);
		newToolsResult.setMessage(
				collectFromUserFilesDirectory.size() + " project(s) have been removed from the user_files directory.");

		return newToolsResult;
	}

	private void renameUserFiles(Path path, final Set<String> userFilesToRemove) throws IOException {
		Files.list(path).filter(t -> t.getFileName().toString().contains("_"))
				.filter(t -> userFilesToRemove.contains(t.getFileName().toString().split("_")[0])).forEach(t -> {
					try {
						Files.move(t, t.resolveSibling("OLD" + t.getFileName().toString()));
					} catch (IOException e) {
						throw new WebApplicationException(
								"Problem to rename unused files from the user_files directory.",
								Status.INTERNAL_SERVER_ERROR);
					}
				});

	}
}
