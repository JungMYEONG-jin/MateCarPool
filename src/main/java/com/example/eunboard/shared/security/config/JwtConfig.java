package com.example.eunboard.shared.security.config;

import com.example.eunboard.shared.security.JwtAuthenticationFilter;
import com.example.eunboard.shared.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 11/16 redis template 추가
 */
@RequiredArgsConstructor
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void configure(HttpSecurity http){
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider, redisTemplate);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

}
