package fr.mickaelbaron.mysharelatexmanager.service;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ApplicationPath("/")
public class MySharelatexManagerApplication extends Application {

	public MySharelatexManagerApplication() {
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(AuthenticationResourceImpl.class);
		s.add(BearerTokenFilter.class);
		s.add(CrossDomainFilter.class);
		s.add(ProjectResourceImpl.class);
		s.add(UserResourceImpl.class);
		s.add(ToolsResourceImpl.class);
		return s;
	}
}
