package br.security.control;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import br.security.jwt.JWTCredential;

@ApplicationScoped
public class JWTIdentityStore implements IdentityStore {

	@Override
	public CredentialValidationResult validate(Credential credential) {
		if (credential instanceof JWTCredential) {
			// this means we had a valid token
			JWTCredential jwtCredential = (JWTCredential) credential;

			return new CredentialValidationResult(jwtCredential.getCaller(), jwtCredential.getGroups());
		}

		return CredentialValidationResult.INVALID_RESULT;
	}

}
