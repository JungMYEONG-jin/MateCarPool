package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;


    @Test
    public void 테스트() throws Exception {
        //given
        Ticket build = Ticket.builder()
                .build();

        ticketRepository.save(build);
        //when

        //then

    }
}
