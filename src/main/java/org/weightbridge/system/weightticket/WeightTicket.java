package org.weightbridge.system.weightticket;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
public class WeightTicket {

    private static final Logger LOG = LoggerFactory.getLogger(WeightTicket.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Version
    private Integer version  = 0;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ticket_id")
//    private Ticket ticket;

    private UUID ticketNumber;
    private BigDecimal grossWeight;
    private BigDecimal tareWeight;
    private BigDecimal nettWeight;
    private LocalDateTime ticketGrossWeightDateTime;
    private LocalDateTime ticketTareWeightDateTime;

    public WeightTicket() {

    }

    public WeightTicket(UUID id, UUID ticketNumber, BigDecimal grossWeight, LocalDateTime ticketGrossWeightDateTime,BigDecimal tareWeight,
                        LocalDateTime ticketTareWeightDateTime, BigDecimal nettWeight, Integer version ) {
        this.id = id;
        this.ticketNumber = ticketNumber;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(UUID ticketNumber) {
        this.ticketNumber = ticketNumber;
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

    public BigDecimal getNettWeight() {
        return nettWeight;
    }

    public void setNettWeight(BigDecimal grossWeight, BigDecimal tareWeight) {
        this.nettWeight = (this.grossWeight.subtract(this.tareWeight));;
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

    @Override
    public String toString() {
        return "WeightTicket{" +
                "id=" + id +
                ", version=" + version +
                ", ticketNumber=" + ticketNumber +
                ", grossWeight=" + grossWeight +
                ", tareWeight=" + tareWeight +
                ", nettWeight=" + nettWeight +
                ", ticketGrossWeightDateTime=" + ticketGrossWeightDateTime +
                ", ticketTareWeightDateTime=" + ticketTareWeightDateTime +
                '}';
    }
}
