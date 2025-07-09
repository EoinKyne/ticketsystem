package org.weightbridge.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.weightbridge.system.ticket.Ticket;
import org.weightbridge.system.ticketrepository.TicketRepositoryInt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private static final Logger LOG = LoggerFactory.getLogger(TicketController.class);
    private final TicketRepositoryInt ticketRepository;

    public TicketController(TicketRepositoryInt ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    void create(@RequestBody Ticket ticket){
        LOG.info("ticket details: {} ", ticket);
        ticketRepository.save(ticket);
    }

    @GetMapping("/getall")
    List<Ticket> findAll(){
        LOG.info("Return all tickets");
        return ticketRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public Ticket findTicketById(@PathVariable UUID id){
        LOG.info("Return ticket by id {} ", id);
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(ticket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket {} not found. " + id);
        }
        return ticket.get();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/update/grossweight/{id}")
    public void updateGrossWeightById(@RequestBody Ticket ticket, @PathVariable UUID id){
        LOG.info("Update ticket {} with gross weight {} ", id);
        Optional<Ticket> existing = ticketRepository.findById(id);
        if(existing.isPresent()){
            Ticket updatedTicket = new Ticket(existing.get().getId(),
                    existing.get().getLocation(),
                    existing.get().getTicketCreationDateTime(),
                    existing.get().getProduct(),
                    existing.get().getDestination(),
                    existing.get().getVersion());
            LOG.info("Updated ticket {} ", updatedTicket);
            ticketRepository.save(updatedTicket);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket {} not found. " + id);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/update/tareweight/{id}")
    public void updateTareWeightById(@RequestBody Ticket ticket, @PathVariable UUID id){
        LOG.info("Update ticket {} with tare weight {} ", id);
        Optional<Ticket> existing = ticketRepository.findById(id);
        if(existing.isPresent()){
            Ticket updatedTicket = new Ticket(existing.get().getId(),
                    existing.get().getLocation(),
                    ticket.getTicketCreationDateTime(),
                    existing.get().getProduct(),
                    existing.get().getDestination(),
                    existing.get().getVersion());
            LOG.info("Updated ticket {} ", updatedTicket);
            LOG.info("Existing ticket {} ", existing);
            ticketRepository.save(updatedTicket);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket {} not found. " + id);
        }
    }
}
