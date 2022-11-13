package com.example.eunboard.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      // get token
      String token = parseBearerToken(request);
      log.info("JwtAuth Filter, Request URI : {}", request.getRequestURI());

      // 유효 검증
      if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Long memberId = tokenProvider.validateAndGetMemberId(token);
//        log.info("Authenticated member ID : " + memberId);
//
//        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//            memberId,
//            null,
//            AuthorityUtils.NO_AUTHORITIES);
//
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(securityContext);
      }
    } catch (Exception e) {
      logger.error("Could not set member authentication in security context", e);
    }
    filterChain.doFilter(request, response);

  }

  private String parseBearerToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7); // apple api 예로 "Bearer " + jwt 로 보냄
    }
    return null;
  }

}
