package com.jwtsecurity.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.jwtsecurity.model.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Component
public class JwtGenerator {

	public String generate(JwtUser jwtUser) {
		if (!(jwtUser.getUserName().equalsIgnoreCase("srikanth") && jwtUser.getRole().equalsIgnoreCase("admin"))) {

			throw new RuntimeException("Invalid user Can't generate Token !!");
		}
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// We will sign our JWT with our ApiKey secret
		Key secret = MacProvider.generateKey(signatureAlgorithm);
		byte[] apiKeySecretBytes = secret.getEncoded();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		Claims claims = Jwts.claims().setSubject(jwtUser.getUserName());
		claims.put("userId", String.valueOf(jwtUser.getId()));
		claims.put("role", jwtUser.getRole());
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setClaims(claims).signWith(signatureAlgorithm, "youtube");
		builder.setExpiration(new Date(nowMillis + 60000));

		return builder.compact();
	}
}
