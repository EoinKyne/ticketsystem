package org.weightbridge.system.ticketrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weightbridge.system.weightticket.WeightTicket;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface WeightTicketRepositoryIpml extends JpaRepository<WeightTicket, UUID> {

    Optional<WeightTicket> findByTicketNumber(UUID uuid);

}
