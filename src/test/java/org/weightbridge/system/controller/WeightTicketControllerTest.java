package org.weightbridge.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.ticketrepository.WeightTicketRepositoryIpml;
import org.weightbridge.system.weightticket.WeightTicket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeightTicketController.class)
class WeightTicketControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WeightTicketController.class);

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    WeightTicketRepositoryIpml weightTicketRepository;


    private final List<WeightTicket> weightTicketList = new ArrayList<>();
    private final List<Ticket> ticketList = new ArrayList<>();
    @BeforeEach
    void setUp(){
        weightTicketList.add(new WeightTicket(UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), new BigDecimal(0.00),
                LocalDateTime.now(), BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00),  0));
        weightTicketList.add(new WeightTicket (UUID.fromString("30d4b3d9-d91e-4f2a-96c8-968854dc4ecf"), UUID.fromString("2cd54db4-7e9d-45b0-947d-8c4fc1fcc080"), new BigDecimal(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00),  0));
        weightTicketList.add(new WeightTicket (UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), UUID.fromString("2cf54db4-7e9d-45b0-947d-8c4fc1fcc080"), new BigDecimal(0.00),
                LocalDateTime.now(), BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0));
        ticketList.add(new Ticket (UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), "D1 X1", LocalDateTime.now(), "Apples", "Europe", 0));
        ticketList.add(new Ticket (UUID.fromString("30d4b3d9-d91e-4f2a-96c8-968854dc4ecf"), "D2 X1", LocalDateTime.now(), "Oranges", "USA",  0));
        ticketList.add(new Ticket (UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), "D3 X2", LocalDateTime.now(), "Pears", "Australia", 0));
    }

    @Test
    void shouldFindAllWeightTickets() throws Exception{
        when(weightTicketRepository.findAll()).thenReturn(weightTicketList);
        LOG.info("Returned tickets {}", weightTicketList);
        mvc.perform(get("/api/weightticket/getall")).andExpect(status().isOk());
    }

    @Test
    void shouldFindWeightTicketById() throws Exception{
        WeightTicket weightTicket = weightTicketList.get(1);
        LOG.info("Weight Ticket by Id {} ", weightTicket.getId());
        LOG.info("Weight Ticket id {} ", weightTicket.getId());
        when(weightTicketRepository.findById(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(weightTicket));
        mvc.perform(get("/api/weightticket/getbyid/30d4b3d9-d91e-4f2a-96c8-968854dc4ecf")).andExpect(status().isOk());
    }

    @Test
    void shouldFindTicketByTicketNumber() throws Exception{
        WeightTicket weightTicket = weightTicketList.get(1);
        LOG.info("Weight ticket ticket number {} ", weightTicket.getTicketNumber());
        when(weightTicketRepository.findByTicketNumber(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(weightTicket));
        mvc.perform(get("/api/weightticket/getbyticketnumber/2cd54db4-7e9d-45b0-947d-8c4fc1fcc080")).andExpect(status().isOk());
    }

    @Test
    void shouldThrowANotFoundExceptionIfIdDoesNotExist() throws Exception {
        mvc.perform(get("/api/weightticket/getbyid/2ce54db4-7e9d-66b0-947d-8c4fc1fcc080")).andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateFirstWeightByTicketId() throws Exception {
        LOG.info("Test create new weight ticket for Weight");
        WeightTicket weightTicket = new WeightTicket (null, UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), BigDecimal.valueOf(46.000), LocalDateTime.now(),
                BigDecimal.valueOf(0.000), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0);
        LOG.info("Create weight ticket {} ", weightTicket);
        mvc.perform(post("/api/weightticket/firstweight/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weightTicket)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateSecondWeightByTicketId() throws Exception {
        LOG.info("Test update weight ticket for tare weight");
        WeightTicket updatedWeightTicket = new WeightTicket(UUID.fromString("2ce54db4-7e9d-45b0-947d-8c4fc1fcc080"), UUID.fromString("749ccd08-61d0-42cb-aa51-e4dea6a7d97d"), BigDecimal.valueOf(46.000), LocalDateTime.now(),
                BigDecimal.valueOf(16.000), LocalDateTime.now(), BigDecimal.valueOf(30.00), 1);
        WeightTicket weightTicket = weightTicketList.get(2);
        when(weightTicketRepository.findByTicketNumber(ArgumentMatchers.any(UUID.class))).thenReturn(Optional.of(weightTicket));
        when(weightTicketRepository.save(updatedWeightTicket)).thenReturn(updatedWeightTicket);
        mvc.perform(patch("/api/weightticket/update/secondweight/749ccd08-61d0-42cb-aa51-e4dea6a7d97d")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWeightTicket)))
                .andExpect(status().isNoContent());
    }
}
