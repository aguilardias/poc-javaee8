package br.security.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import br.security.jwt.JWTStore;

@ApplicationScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {

	private static final String BEARER = "Bearer ";

	@Inject
	IdentityStore identityStore;

	@Inject
	JWTStore jwtStore;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest req, HttpServletResponse res,
			HttpMessageContext context) throws AuthenticationException {

		String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
		Credential credential = null;

		if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
			String token = authorizationHeader.substring(BEARER.length());

			credential = this.jwtStore.getCredential(token);
		}

		if (credential != null) {
			return context.notifyContainerAboutLogin(this.identityStore.validate(credential));
		} else {
			return context.doNothing();
		}
	}

}
