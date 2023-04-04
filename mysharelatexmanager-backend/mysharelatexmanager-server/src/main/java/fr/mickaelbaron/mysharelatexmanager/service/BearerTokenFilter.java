package fr.mickaelbaron.mysharelatexmanager.service;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import fr.mickaelbaron.mysharelatexmanager.AuthToken;
import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant;
import fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerUtil;
import fr.mickaelbaron.mysharelatexmanager.api.TokenAuthenticated;
import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@TokenAuthenticated
public class BearerTokenFilter implements ContainerRequestFilter {

	@Inject
	IConfigExecution refConfiguration;

	private boolean debug = true;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		final AuthToken decryptToken = MySharelatexManagerUtil.decryptToken(token,
				refConfiguration.getEncryptPassword(), refConfiguration.getEncryptNoise());

		if (debug) {
			return;
		}

		if (decryptToken != null) {
			final Date startDate = decryptToken.getStartDate();

			if (startDate != null) {
				final long diff = new Date().getTime() - startDate.getTime();
				if ((diff / 1000) < getSessionTimeOut(refConfiguration.getSessionTimeout())) {
					if (!(decryptToken.getIdentifiant().equals(refConfiguration.getIdentificationUser()))) {
						throw new WebApplicationException("Token is wrong.", Status.UNAUTHORIZED);
					}
				} else {
					throw new WebApplicationException("Session timeout.", Status.UNAUTHORIZED);
				}
			} else {
				throw new WebApplicationException("Token is wrong.", Status.UNAUTHORIZED);
			}
		} else {
			throw new WebApplicationException("User is not unauthorized.", Status.UNAUTHORIZED);
		}
	}

	public static int getSessionTimeOut(String configurationValue) {
		try {
			return 60 * Integer.parseInt(configurationValue);
		} catch (NumberFormatException e) {
			return MySharelatexManagerConstant.SESSION_TIMEOUT_DEFAULT;
		}
	}
}