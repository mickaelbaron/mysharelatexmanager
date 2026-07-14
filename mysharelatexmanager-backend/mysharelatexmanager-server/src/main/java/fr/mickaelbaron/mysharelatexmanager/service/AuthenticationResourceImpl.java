package fr.mickaelbaron.mysharelatexmanager.service;

import java.util.Date;

import fr.mickaelbaron.mysharelatexmanager.AuthToken;
import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerUtil;
import fr.mickaelbaron.mysharelatexmanager.api.AuthenticationResource;
import fr.mickaelbaron.mysharelatexmanager.api.NotYetImplementedException;
import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;
import fr.mickaelbaron.mysharelatexmanager.entity.EntityFactory;
import fr.mickaelbaron.mysharelatexmanager.model.Credentials;
import fr.mickaelbaron.mysharelatexmanager.model.CredentialsResult;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@RequestScoped
public class AuthenticationResourceImpl implements AuthenticationResource {

	@Inject
	IConfigExecution refConfiguration;

	@Override
	public CredentialsResult login(Credentials credentials) {
		if (credentials == null || credentials.getUsername() == null || credentials.getUsername().isEmpty()
				|| credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
			throw new WebApplicationException("Parameter is missing.", Status.BAD_REQUEST);
		}

		if (!refConfiguration.getIdentificationUser().equals(credentials.getUsername())) {
			throw new WebApplicationException("Invalid Login.", Status.UNAUTHORIZED);
		}

		final String hashPassword = MySharelatexManagerUtil.hashPassword(refConfiguration.getIdentificationPassword());
		if (MySharelatexManagerUtil.checkPassword(credentials.getPassword(), hashPassword)) {
			AuthToken currentToken = new AuthToken(credentials.getUsername(), new Date());
			final String encryptToken = MySharelatexManagerUtil.encryptToken(currentToken,
					refConfiguration.getEncryptPassword(), refConfiguration.getEncryptNoise());

			return EntityFactory.createCredentialsResult(credentials.getUsername(), encryptToken);
		} else {
			throw new WebApplicationException("Invalid Password.", Status.UNAUTHORIZED);
		}
	}

	@Override
	public Response logout() {
		throw new NotYetImplementedException();
	}
}
