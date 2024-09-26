package org.weightbridge.system.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
public class Ticket {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @NotEmpty
    private String location;
    private LocalDateTime ticketCreationDateTime;
    @Version
    private Integer version;

    public Ticket(){

    }

    public Ticket(UUID id, String location, LocalDateTime ticketCreationDateTime, Integer version) {
        this.id = id;
        this.location = location;
        this.ticketCreationDateTime = ticketCreationDateTime;
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

