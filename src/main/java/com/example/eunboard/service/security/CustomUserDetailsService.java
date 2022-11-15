package com.example.eunboard.service.security;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.adapter.out.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByPhoneNumber(username).map(this::createUserDetails)
                .orElseThrow(()-> new UsernameNotFoundException(username +" -> 일치하는 정보가 존재하지 않습니다."));
    }

    private UserDetails createUserDetails(Member member){
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getAuth().toString());
        return new User(String.valueOf(member.getMemberId()),
                member.getPassword(),
                Collections.singleton(authority));
    }


}
