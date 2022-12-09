package com.example.eunboard.ticket.adapter.in;

import com.example.eunboard.ticket.domain.Ticket;

import java.util.List;

public interface CustomTicketRepository {
     List<Ticket> findAll();
     /**
      * 오전9시30~오후8시55까지 만들어진걸
      * 오후 9시에 보여준다
      * @return
      */
     List<Ticket> getAvailableList();
     boolean existTicket(Long memberId);
}
