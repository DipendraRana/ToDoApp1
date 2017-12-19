package com.bridgelabz.service;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface TokensOperation {
	
	public String generateToken(String subject,Key key);
	
	public String generateTokenWithExpire(String subject,Key key,long milliseconds,int userId);
	
	public Claims parseTheToken(Key key,String token) throws ExpiredJwtException;

}
