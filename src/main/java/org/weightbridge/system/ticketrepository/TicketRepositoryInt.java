package org.weightbridge.system.ticketrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.weightticket.WeightTicket;

import java.util.UUID;

@Repository
public interface TicketRepositoryInt extends JpaRepository<Ticket, UUID> {
}
