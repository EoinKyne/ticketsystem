package org.weightbridge.system.controller;

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
import org.testcontainers.junit.jupiter.Testcontainers;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.ticketrepository.TicketRepositoryInt;
import org.weightbridge.system.util.PostgresContainer;

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
    static PostgreSQLContainer<PostgresContainer> postgreSQLContainer = PostgresContainer.getInstance();
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
        LOG.info("Test established connection");
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldCreateNewTicket(){
        LOG.info("Test should create new ticket");
        Ticket newTicket = new Ticket(null, "rr r5", LocalDateTime.now(), "ice", "Iceland", 0 );
        ResponseEntity<Void> ticket = restClient.post()
                .uri("/api/ticket/create")
                .body(newTicket)
                .retrieve()
                .toBodilessEntity();
        assertEquals(201, ticket.getStatusCode().value());
    }

    @Test
    void shouldFindAllTickets(){
        LOG.info("Test should find all tickets");
        ticketRepositoryInt.save(new Ticket(null, "r2 d2", LocalDateTime.now(), "eggs", "Antarctica", 0));
        ticketRepositoryInt.save(new Ticket(null, "r3 d4", LocalDateTime.now(), "snow", "Greenland", 0));
        ticketRepositoryInt.flush();
        long transactions = ticketRepositoryInt.count();
        LOG.info("Transaction count: ", transactions);

        List<Ticket> ticketList = restClient.get()
                .uri("/api/ticket/getall")
                .retrieve()
                .body(new ParameterizedTypeReference<>(){
                });
        LOG.debug("Ticket List {} : ", ticketList);
        assertEquals(ticketList.size(), ticketRepositoryInt.count());
    }

    @Test
    void shouldFindTicketById() {
        LOG.info("Test should find by ticket id");
        Ticket ticket = new Ticket(null, "r4 d2", LocalDateTime.now(), "Snails", "Wales", 0);
        ticketRepositoryInt.save(ticket);
        ticketRepositoryInt.flush();
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticketRepositoryInt.findAll().get(0));
        UUID ticketIdFromDb = ticketList.get(0).getId();
        Ticket ticket1 = restClient.get().uri("/api/ticket/get/" + ticketIdFromDb).retrieve().body(Ticket.class);
        assertAll(
                () -> assertEquals(ticketIdFromDb, ticket1.getId()),
                () -> assertEquals("r4 d2", ticket1.getLocation()),
                () -> assertEquals("Snails", ticket1.getProduct()),
                () -> assertEquals("Wales", ticket1.getDestination())
        );
    }
}
