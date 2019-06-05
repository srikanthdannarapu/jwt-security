package com.jwtsecurity.security;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.jwtsecurity.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Component
public class JwtValidator {

	// The JWT signature algorithm we will be using to sign the token
	SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// We will sign our JWT with our ApiKey secret
	Key secret = MacProvider.generateKey(signatureAlgorithm);
	byte[] apiKeySecretBytes = secret.getEncoded();
	Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

	public JwtUser validate(String token) {

		JwtUser jwtUser = null;
		try {
			Claims body = Jwts.parser().setSigningKey("youtube").parseClaimsJws(token).getBody();
			System.out.println(body.get("userId"));

			jwtUser = new JwtUser();

			jwtUser.setUserName(body.getSubject());
			jwtUser.setId(Long.parseLong((String) body.get("userId")));
			jwtUser.setRole((String) body.get("role"));
		} catch (Exception e) {
			System.out.println(e);
		}

		return jwtUser;
	}
}
