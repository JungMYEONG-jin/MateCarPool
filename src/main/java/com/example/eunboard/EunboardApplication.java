package com.example.eunboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EunboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(EunboardApplication.class, args);
    }

}
