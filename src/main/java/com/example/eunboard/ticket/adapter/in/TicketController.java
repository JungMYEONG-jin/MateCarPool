package com.example.eunboard.ticket.adapter.in;

import com.example.eunboard.ticket.application.port.in.TicketRequestDTO;
import com.example.eunboard.ticket.application.port.in.TicketResponseDTO;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.example.eunboard.ticket.application.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ticket")
@RestController
public class TicketController {

  private final TicketService ticketService;


  @PostMapping("/new")
  public void ticketCreate(@RequestBody TicketRequestDTO requestDTO) {
    ticketService.ticketSave(requestDTO);
  }

  @ResponseBody
  @GetMapping("/list")
  public List<TicketResponseDTO> findAll() {
    return ticketService.findAll();
  }

  @ResponseBody
  @GetMapping("/promise")
  public TicketResponseDTO promise(@AuthenticationPrincipal Long memberId) {
      return ticketService.ticketPromise(memberId);
  }

  @ResponseBody
  @GetMapping("/read/{id}")
  public TicketResponseDTO read(@PathVariable long id) {
    return ticketService.ticketRead(id);
  }

  @GetMapping("/update/{id}")
  public void delete(@PathVariable long id, TicketStatus status) {
    ticketService.ticketStatusUpdate(id, status);
  }
  
}
