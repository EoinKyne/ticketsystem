package org.weightbridge.system.ticket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private BigDecimal grossWeight;
    private BigDecimal tareWeight;
    private BigDecimal nettWeight;
    private LocalDateTime ticketGrossWeightDateTime;
    private LocalDateTime ticketTareWeightDateTime;

    public Ticket(){

    }

    public Ticket(UUID id,  String location, LocalDateTime ticketCreationDateTime, BigDecimal grossWeight, LocalDateTime ticketGrossWeightDateTime,
                  BigDecimal tareWeight, LocalDateTime ticketTareWeightDateTime, BigDecimal nettWeight, Integer version) {
        this.id = id;
        this.location = location;
        this.ticketCreationDateTime = ticketCreationDateTime;
        this.grossWeight = grossWeight;
        this.ticketGrossWeightDateTime = ticketGrossWeightDateTime;
        this.tareWeight = tareWeight;
        this.ticketTareWeightDateTime = ticketTareWeightDateTime;
        this.nettWeight = nettWeight;
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


    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(BigDecimal tareWeight) {
        this.tareWeight = tareWeight;
    }

    public LocalDateTime getTicketGrossWeightDateTime() {
        return ticketGrossWeightDateTime;
    }

    public void setTicketGrossWeightDateTime(LocalDateTime ticketGrossWeightDateTime) {
        this.ticketGrossWeightDateTime = ticketGrossWeightDateTime;
    }

    public LocalDateTime getTicketTareWeightDateTime() {
        return ticketTareWeightDateTime;
    }

    public void setTicketTareWeightDateTime(LocalDateTime ticketTareWeightDateTime) {
        this.ticketTareWeightDateTime = ticketTareWeightDateTime;
    }

    public BigDecimal getNettWeight() {
        return nettWeight;
    }

    public void setNettWeight(BigDecimal grossWeight, BigDecimal tareWeight) {
        this.nettWeight = (this.grossWeight.subtract(this.tareWeight));
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", ticketCreationDateTime=" + ticketCreationDateTime +
                ", version=" + version +
                ", grossWeight=" + grossWeight +
                ", tareWeight=" + tareWeight +
                ", nettWeight=" + nettWeight +
                ", grossWeightDateTime=" + ticketGrossWeightDateTime +
                ", tareWeightDateTime=" + ticketTareWeightDateTime +
                '}';
    }
}

