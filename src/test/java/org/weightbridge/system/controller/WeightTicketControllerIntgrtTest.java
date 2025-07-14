package org.weightbridge.system.controller;


import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.weightbridge.system.ticketrepository.WeightTicketRepositoryIpml;
import org.weightbridge.system.util.PostgresContainer;
import org.weightbridge.system.weightticket.WeightTicket;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.ticketrepository.TicketRepositoryInt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeightTicketControllerIntgrtTest {

    private static final Logger LOG = LoggerFactory.getLogger(WeightTicketControllerIntgrtTest.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<PostgresContainer> postgreSQLContainer = PostgresContainer.getInstance();
    @LocalServerPort
    int randomServerPort;
    RestClient restClient;

    @Autowired
    TicketRepositoryInt ticketRepository;

    @Autowired
    WeightTicketRepositoryIpml weightTicketRepository;

    List<Ticket> ticketList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        restClient = RestClient.create("http://localhost:" + randomServerPort);
        ticketRepository.save(new Ticket(null, "r2 d2", LocalDateTime.now(), "eggs", "Antarctica", 0));
        ticketRepository.save(new Ticket(null, "r2 d2", LocalDateTime.now(), "eggs", "Antarctica", 0));
        ticketRepository.flush();
        ticketList.add(ticketRepository.findAll().get(0));
        ticketList.add(ticketRepository.findAll().get(1));
    }

    @Test
    void isConnectionEstablished(){
        LOG.info("Test is connection established");
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllWeightTickets(){
        LOG.info("Should find all weight tickets");

        UUID ticketId1 = ticketList.get(0).getId();
        UUID ticketId2 = ticketList.get(1).getId();

        weightTicketRepository.save(new WeightTicket(null, ticketId1, BigDecimal.valueOf(0.0), LocalDateTime.now(),BigDecimal.valueOf(0.0), LocalDateTime.now(),
                BigDecimal.valueOf(0.0), 0));
        weightTicketRepository.save(new WeightTicket(null, ticketId2, BigDecimal.valueOf(0.0), LocalDateTime.now(), BigDecimal.valueOf(0.0), LocalDateTime.now(),
                BigDecimal.valueOf(0.0),0));
        weightTicketRepository.flush();
        long transactions = weightTicketRepository.count();
        LOG.debug("Weight Ticket repo {}", weightTicketRepository.findAll());

        List<WeightTicket> weightTicketList = restClient.get()
                .uri("/api/weightticket/getall")
                .retrieve()
                .body(new ParameterizedTypeReference<>(){
                });
        LOG.debug("Weight Ticket List {} : ", weightTicketList);
        assertEquals(transactions, weightTicketRepository.count());
    }

    @Test
    void shouldFindWeightTicketById() {
        LOG.info("Should find weight tickets by id");
        UUID ticketId1 = ticketList.get(0).getId();
        WeightTicket weightTicket = new WeightTicket(null, ticketId1, BigDecimal.valueOf(46.0), LocalDateTime.now(), BigDecimal.valueOf(0.0),
                LocalDateTime.now(), BigDecimal.valueOf(0.0), 0);
        weightTicketRepository.save(weightTicket);
        weightTicketRepository.flush();
        List<WeightTicket> weightTicketList = new ArrayList<>();
        weightTicketList.add(weightTicketRepository.findAll().get(0));
        UUID ticketIdFromDb = weightTicketList.get(0).getId();
        WeightTicket weightTicket1 = restClient.get().uri("/api/weightticket/getbyid/" + ticketIdFromDb).retrieve().body(WeightTicket.class);
        assertAll(
                () -> assertEquals(ticketIdFromDb, weightTicket1.getId()),
                () -> assertEquals(BigDecimal.valueOf(46.0), weightTicket.getGrossWeight()),
                () -> assertEquals(ticketId1, weightTicket.getTicketNumber())
        );
    }
    @Test
    void shouldFindAndUpdateFirstweightOfWeightTicket(){
        LOG.info("Should find and update firstweight of weight ticket");
        UUID ticketId1 = ticketList.get(0).getId();
        WeightTicket weightTicket = new WeightTicket(null, ticketId1, BigDecimal.valueOf(45.5), LocalDateTime.now(), BigDecimal.valueOf(0.0), LocalDateTime.now(),
                BigDecimal.valueOf(0.0), 0);

        ResponseEntity<Void> updatedWeightTicket = restClient.post()
                .uri("/api/weightticket/firstweight/create")
                .body(weightTicket)
                .retrieve()
                .toBodilessEntity();
        assertEquals(201, updatedWeightTicket.getStatusCode().value());
    }

    @Test
    void shouldFindAndUpdateSecondweightOfWeightTicket(){
        LOG.info("Should find and update secondweight of weight ticket");
        UUID ticketId1 = ticketList.get(0).getId();

        WeightTicket weightTicket = new WeightTicket(null, ticketId1, BigDecimal.valueOf(45.5), LocalDateTime.now(), BigDecimal.valueOf(14.0), LocalDateTime.now(),
                BigDecimal.valueOf(0.0), 0);

        ResponseEntity<Void> updatedWeightTicket = restClient.patch()
                .uri("/api/weightticket/update/secondweight/" + ticketId1)
                .body(weightTicket)
                .retrieve()
                .toBodilessEntity();
        assertEquals(204, updatedWeightTicket.getStatusCode().value());
    }
}
