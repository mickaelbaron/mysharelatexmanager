package fr.mickaelbaron.mysharelatexmanager.service;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Provider
public class CrossDomainFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext cresp) throws IOException {
		cresp.getHeaders().add("Access-Control-Allow-Origin", "*");
		cresp.getHeaders().add("Access-Control-Allow-Credentials", "true");
		cresp.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		cresp.getHeaders().add("Allow", "GET, POST, DELETE, PUT");
		cresp.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept, authorization");
	}
}
