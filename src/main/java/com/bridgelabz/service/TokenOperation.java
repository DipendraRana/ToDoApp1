package com.bridgelabz.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface TokenOperation {
	
	public String generateToken(String subject,String key);
	
	public String generateExpiryToken(String subject, String key,long milliseconds);
	
	public String generateTokenWithExpire(String claim,String claimName,String key,long milliseconds,int userId);
	
	public Claims parseTheToken(String key,String token) throws ExpiredJwtException;
	
	public <T> String generateToken(String claimName,T object,String key);

}
