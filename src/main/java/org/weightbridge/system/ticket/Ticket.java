package org.weightbridge.system.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weightbridge.system.weightticket.WeightTicket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Ticket {

    private static final Logger LOG = LoggerFactory.getLogger(Ticket.class);

    @Id
    //@UuidGenerator(style = UuidGenerator.Style.RANDOM)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private UUID id;
    @NotEmpty
    private String location;
    //@JsonFormat(pattern = "YYYY-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDateTime ticketCreationDateTime;
    @Version
    private Integer version = 0;

    private String product;
    private String destination;
//    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "weightTicket_id")
//    private WeightTicket weightTicket;

    public Ticket(){

    }

    public Ticket(UUID id,  String location, LocalDateTime ticketCreationDateTime,  String product, String destination,
                   Integer version) {
        this.id = id;
        this.location = location;
        this.ticketCreationDateTime = ticketCreationDateTime;
        this.product = product;
        this.destination = destination;
        this.version = version;
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTicketCreationDateTime() {
        return ticketCreationDateTime;
    }

    public void setTicketCreationDateTime(LocalDateTime ticketCreationDateTime) {
        this.ticketCreationDateTime = ticketCreationDateTime;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", ticketCreationDateTime=" + ticketCreationDateTime +
                ", version=" + version +
                '}';
    }
}

