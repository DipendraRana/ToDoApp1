package com.bridgelabz.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenOperationImplement implements TokenOperation {

	@Override
	public String generateToken(String subject, String key) {
		return Jwts.builder().setSubject(subject).signWith(SignatureAlgorithm.HS512,key).compact();
	}
	
	@Override
	public String generateExpiryToken(String subject, String key,long milliseconds) {
		long nowMillisecond = System.currentTimeMillis();
	    long expiryMillisecond = nowMillisecond + milliseconds;
		return Jwts.builder().setExpiration(new Date(expiryMillisecond)).setSubject(subject).signWith(SignatureAlgorithm.HS512,key).compact();
	}

	@Override
	public String generateTokenWithExpire(String claim,String claimName, String key, long milliseconds,int userId) {
		long nowMillisecond = System.currentTimeMillis();
	    long expiryMillisecond = nowMillisecond + milliseconds;
		return Jwts.builder().setExpiration(new Date(expiryMillisecond)).setSubject(String.valueOf(userId)).claim(claimName, claim).signWith(SignatureAlgorithm.HS512,key).compact();
	}

	@Override
	public Claims parseTheToken(String key, String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}
	
	@Override
	public <T> String generateToken(String claimName,T object,String key) {
		return Jwts.builder().claim(claimName, object).signWith(SignatureAlgorithm.HS512,key).compact();
	}

}