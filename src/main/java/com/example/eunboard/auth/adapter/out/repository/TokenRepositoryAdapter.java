//package com.example.eunboard.auth.adapter.out.repository;
//
//import com.example.eunboard.auth.application.port.out.TokenRepositoryPort;
//import com.example.eunboard.auth.domain.RefreshToken;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class TokenRepositoryAdapter implements TokenRepositoryPort {
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    @Override
//    public RefreshToken save(RefreshToken refreshToken) {
//        return refreshTokenRepository.save(refreshToken);
//    }
//
//    @Override
//    public Optional<RefreshToken> findByKey(String key) {
//        return refreshTokenRepository.findByKey(key);
//    }
//}
