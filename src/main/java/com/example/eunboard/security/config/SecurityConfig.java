package com.example.eunboard.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.example.eunboard.security.JwtAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /** TODO: 프로덕트레벨에서 관리필요 */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/**")
                //.authorizeRequests().antMatchers("/kakaoLogin", "/member/profile/**") // 카카오로그인만 접근허용되어있음
                .permitAll()
                .anyRequest().authenticated();


        http.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class);

        return http.build();
    }

}
