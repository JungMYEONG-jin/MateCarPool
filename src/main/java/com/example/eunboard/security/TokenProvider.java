package com.example.eunboard.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.eunboard.domain.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {

  private static final String SECRET_KEY = "NMA8JPctFuna59f5";

  public String create(Member member) {
    Date expiryDate = Date.from(
        Instant.now().plus(1, ChronoUnit.DAYS));
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .setSubject(Long.toString(member.getMemberId()))
        .setIssuer("carpool app")
        .setIssuedAt(new Date())
        .setExpiration(expiryDate).compact();
  }

  public Long validateAndGetMemberId(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }
}
