package org.weightbridge.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.ticketrepository.TicketRepositoryInt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TicketControllerTest.class);

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TicketRepositoryInt ticketRepositoryInt;
    private final List<Ticket> ticketList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        ticketList.add(new Ticket (UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), "D1 X1", LocalDateTime.now(), new BigDecimal(0.00),
                LocalDateTime.now(), BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00),  0));
        ticketList.add(new Ticket (UUID.fromString("30d4b3d9-d91e-4f2a-96c8-968854dc4ecf"), "D2 X1", LocalDateTime.now(), new BigDecimal(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00),  0));
        ticketList.add(new Ticket (UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), "D3 X2", LocalDateTime.now(), new BigDecimal(0.00),
                LocalDateTime.now(), BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0));
    }

    @Test
    void shouldCreateANewTicket() throws Exception{
        Ticket newTicket = new Ticket (UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc180"), "D1 X1", LocalDateTime.now(), new BigDecimal(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0);
        LOG.info("New ticket {} ", newTicket);
        LOG.info("Big decimal {} ", newTicket.getGrossWeight());
        mvc.perform(post("/api/ticket/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTicket))).andExpect(status().isCreated());
    }

    //
    void shouldNotCreateTheSameStudentWithUsedIdNumber() throws Exception{
        Ticket newTicket =new Ticket(UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), "D2 X2", LocalDateTime.now(), new BigDecimal(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0);
        mvc.perform(post("/api/ticket/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTicket))).andExpect(status().isConflict());
    }

    @Test
    void shouldFindAllTickets() throws Exception{
        when(ticketRepositoryInt.findAll()).thenReturn(ticketList);
        LOG.info("Returned tickets {}", ticketList);
        mvc.perform(get("/api/ticket/getall")).andExpect(status().isOk());
    }

    @Test
    void shouldFindTicketById() throws Exception{
        Ticket ticket = ticketList.get(1);
        LOG.info("Ticket by Id {} ", ticket.getId());
        when(ticketRepositoryInt.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(ticket));
        mvc.perform(get("/api/ticket/get/30d4b3d9-d91e-4f2a-96c8-968854dc4ecf")).andExpect(status().isOk());
    }

    @Test
    void shouldThrowANotFoundExceptionIfIdDoesNotExist() throws Exception {
        mvc.perform(get("/api/ticket/get/2ce54db4-7e9d-45b0-947d-8c4fc1fcc080")).andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateGrossWeightByTicketId() throws Exception {
        LOG.info("Test update ticket for gross Weight");
        Ticket updatedTicket = new Ticket (UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), "D3 X2", LocalDateTime.now(), BigDecimal.valueOf(46.000), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 1);
        LOG.info("Updated ticket {} ", updatedTicket);
        Ticket ticket = ticketList.get(2);
        LOG.info("Found ticket {} ", ticket);
        when(ticketRepositoryInt.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(ticket));
        when(ticketRepositoryInt.save(updatedTicket)).thenReturn(updatedTicket);
        LOG.info("Updated: {} ", updatedTicket);
        mvc.perform(patch("/api/ticket/update/grossweight/749ccd08-61d0-42cb-aa51-e4dea6a7d97d")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateTareWeightByTicketId() throws Exception {
        LOG.info("Test update ticket for tare weight");
        Ticket updatedTicket = new Ticket(UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), "D3 X2", LocalDateTime.now(), BigDecimal.valueOf(46.000), LocalDateTime.now(),
                BigDecimal.valueOf(16.000), LocalDateTime.now(), BigDecimal.valueOf(30.00), 2);
        Ticket ticket = ticketList.get(2);
        when(ticketRepositoryInt.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(ticket));
        when(ticketRepositoryInt.save(updatedTicket)).thenReturn(updatedTicket);
        mvc.perform(patch("/api/ticket/update/tareweight/749ccd08-61d0-42cb-aa51-e4dea6a7d97d")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTicket)))
                .andExpect(status().isNoContent());
    }

}