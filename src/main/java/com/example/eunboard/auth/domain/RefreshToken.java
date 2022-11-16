package com.example.eunboard.auth.domain;

import com.example.eunboard.old.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class RefreshToken extends BaseEntity {

    @Id
    @Column(name = "refresh_key")
    private String key;

    @Column(name="refresh_value")
    private String value;

    @Builder
    public RefreshToken(String key, String value){
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token){
        this.value = token;
        return this;
    }
}
