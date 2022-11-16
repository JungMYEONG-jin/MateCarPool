package com.example.eunboard.shared.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.eunboard.member.domain.Member;

@Slf4j
@Service
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  //private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 테스트 환경 1일
  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 ; // 테스트 환경 1분
  private static final long REAL_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 실제 운영 환경 30분
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

  // 임시
  private static final String SECRET_KEY = "bWF0ZS1jYXJwb29sCg==";

  private final Key key;

  public TokenProvider(@Value("${jwt.secret}") String secretKey){
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto generateToken(Authentication authentication){
    String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    long now = (new Date()).getTime();

    Date exp = new Date(now + TOKEN_EXPIRE_TIME);
    Date refreshExp = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
    // create accessToken
    String accessToken = Jwts.builder().setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(exp)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    String refreshToken = Jwts.builder().setExpiration(refreshExp).signWith(key, SignatureAlgorithm.HS512).compact();

    return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(exp.getTime())
            .refreshTokenExpiresIn(refreshExp.getTime())
            .build();
  }

  public Authentication getAuthentication(String accessToken){
    // decode
    Claims claims = parseClaims(accessToken);

    // no authority
    if (claims.get(AUTHORITIES_KEY)==null){
      throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
    }

    List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal,"",authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

  public Long getExpiration(String accessToken){
    Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
    long now = new Date().getTime();
    return expiration.getTime() - now;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }


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
