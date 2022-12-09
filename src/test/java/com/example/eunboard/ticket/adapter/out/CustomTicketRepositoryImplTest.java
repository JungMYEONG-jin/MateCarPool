package com.example.eunboard.ticket.adapter.out;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomTicketRepositoryImplTest {

    @Test
    void checkTimeLogic() {
        LocalDateTime now = LocalDateTime.now();
        now = now.toLocalDate().atTime(0,1);
        // 만약 새벽~오전 9시30분 전이면 -1씩 해야함
        // 전날 오전9시30~오후9시까지 생성된 티켓을 봐야하기 때문.
        LocalDateTime morning = now.toLocalDate().atTime(9, 30);
        LocalDateTime night = now.toLocalDate().atTime(21, 0);
        if (now.isAfter(now.toLocalDate().atTime(0,0)) && now.isBefore(now.toLocalDate().atTime(9, 30))){
            morning = morning.minusDays(1L);
            night = night.minusDays(1L);
        }
        System.out.println("now = " + now);
        System.out.println("night = " + night);
        System.out.println("morning = " + morning);
    }
}