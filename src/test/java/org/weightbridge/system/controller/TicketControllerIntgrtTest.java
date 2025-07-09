package org.weightbridge.system.controller;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.weightbridge.system.ticket.Ticket;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.weightbridge.system.ticketrepository.TicketRepositoryInt;
import org.weightbridge.system.util.PostgresContainer;

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
public class TicketControllerIntgrtTest {

    private static final Logger LOG = LoggerFactory.getLogger(TicketControllerIntgrtTest.class);

    //@Container
    //@ServiceConnection
    //static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16:3");
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<PostgresContainer> postgreSQLContainer = PostgresContainer.getInstance();
    @LocalServerPort
    int randomServerPort;

    RestClient restClient;
    @Autowired
    TicketRepositoryInt ticketRepositoryInt;

    @BeforeEach
    void setUp(){
        restClient = RestClient.create("http://localhost:" + randomServerPort);
    }

    @Test
    void isConnectionEstablished(){
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllTickets(){
        ticketRepositoryInt.save(new Ticket(null, "r2 d2", LocalDateTime.now(), BigDecimal.valueOf(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.00), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0));
        ticketRepositoryInt.save(new Ticket(null, "r3 d4", LocalDateTime.now(), BigDecimal.valueOf(0.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.00), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0));
        ticketRepositoryInt.flush();
        long transactions = ticketRepositoryInt.count();
        LOG.info("Ticket repo {}", ticketRepositoryInt.findAll());

        List<Ticket> ticketList = restClient.get()
                .uri("/api/ticket/getall")
                .retrieve()
                .body(new ParameterizedTypeReference<>(){
                });
        LOG.info("Ticket List {} : ", ticketList);
        assertEquals(transactions, ticketRepositoryInt.count());
    }

    @Test
    void shouldFindTicketById() {
        Ticket ticket = new Ticket(null, "r4 d2", LocalDateTime.now(), BigDecimal.valueOf(46.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.00), LocalDateTime.now(), BigDecimal.valueOf(0.00), 0);
        ticketRepositoryInt.save(ticket);
        ticketRepositoryInt.flush();
        //LOG.info("Ticket repo {}", ticketRepositoryInt.findAll().get(0));
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticketRepositoryInt.findAll().get(0));
        UUID ticketIdFromDb = ticketList.get(0).getId();
        //LOG.info("Get ticket id {} ", ticketIdFromDb);
        //LOG.info("/api/ticket/get/"+ticketIdFromDb);
        Ticket ticket1 = restClient.get().uri("/api/ticket/get/" + ticketIdFromDb).retrieve().body(Ticket.class);
        //LOG.info("ticket 1 {} and uri {} ", ticket1, restClient.get().uri("/api/ticket/get/"+ticketIdFromDb));
        //LOG.info("ticket 1 get id {} ", ticket1.getId());
        //LOG.info("Ticket 1 get id {} ", ticket1);
        assertAll(
                //() -> assertEquals(ticketIdFromDb, ticket1.getId())
                () -> assertEquals("r4 d2", ticket1.getLocation()),
                () -> assertEquals(BigDecimal.valueOf(46), ticket1.getGrossWeight()),
                () -> assertEquals(BigDecimal.valueOf(0), ticket1.getTareWeight()),
                () -> assertEquals(2, ticket1.getVersion())
        );
    }

    @Test
    void shouldFindAndUpdateGrossweightOfTicket(){
        Ticket ticket = new Ticket(null, "r4 d2", LocalDateTime.now(), BigDecimal.valueOf(46.00), LocalDateTime.now(),
                BigDecimal.valueOf(0.00), LocalDateTime.now(), BigDecimal.valueOf(0.00), 2);
        ticketRepositoryInt.save(ticket);
        ticketRepositoryInt.flush();
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticketRepositoryInt.findAll().get(0));
        UUID ticketIdFromDb = ticketList.get(0).getId();
        LOG.info("/api/ticket/get/"+ticketIdFromDb);
        Ticket ticket1 = restClient.get().uri("/api/ticket/get/"+ticketIdFromDb).retrieve().body(Ticket.class);
        LOG.info("Ticket 1 retieved by restClient {} ", ticket1);
        ResponseEntity<Void> updatedTicket = restClient.patch()
                .uri("/api/ticket/update/grossweight/"+ticketIdFromDb)
                .body(ticket1)
                .retrieve()
                .toBodilessEntity();
        assertEquals(204, updatedTicket.getStatusCodeValue());
    }
}
