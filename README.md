# jwt-security -- Spring Security using JWT (Json Web Token) in Spring Boot

This Project uses JWT to secure the REST endpoints.

The Following are the REST end points available in the example.

/token - Generates the JWT token based on the JSON sent. Its a POST method which expects the JSON: { "username": "name", "id": 123, "role": "admin"}
/rest/hello - Requires a JWT Token with Header key - "Authorisation" and value - "Token <JWT_Token>"

postman scripts are in project folder

# Generate JWT token 
https://stormpath.com/blog/jwt-java-create-verify

# Generate Tokens

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;    
 
#Sample method to construct a JWT


private String createJWT(String id, String issuer, String subject, long ttlMillis) {
 
    //The JWT signature algorithm we will be using to sign the token
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
 
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
 
    //We will sign our JWT with our ApiKey secret
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
 
    //Let's set the JWT Claims
    JwtBuilder builder = Jwts.builder().setId(id)
                                .setIssuedAt(now)
                                .setSubject(subject)
                                .setIssuer(issuer)
                                .signWith(signatureAlgorithm, signingKey);
 
    //if it has been specified, let's add the expiration
    if (ttlMillis >= 0) {
    long expMillis = nowMillis + ttlMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
    }
 
    //Builds the JWT and serializes it to a compact, URL-safe string
    return builder.compact();
}




# Decode and Verify Tokens

import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

#Sample method to validate and read the JWT

private void parseJWT(String jwt) {

    //This line will throw an exception if it is not a signed JWS (as expected)
    Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
       .parseClaimsJws(jwt).getBody();
    System.out.println("ID: " + claims.getId());
    System.out.println("Subject: " + claims.getSubject());
    System.out.println("Issuer: " + claims.getIssuer());
    System.out.println("Expiration: " + claims.getExpiration());
}

import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
 
#Sample method to validate and read the JWT

private void parseJWT(String jwt) {
 
    //This line will throw an exception if it is not a signed JWS (as expected)
    Claims claims = Jwts.parser()         
       .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
       .parseClaimsJws(jwt).getBody();
    System.out.println("ID: " + claims.getId());
    System.out.println("Subject: " + claims.getSubject());
    System.out.println("Issuer: " + claims.getIssuer());
    System.out.println("Expiration: " + claims.getExpiration());
}