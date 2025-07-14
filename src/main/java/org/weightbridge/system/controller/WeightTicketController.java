package org.weightbridge.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.weightbridge.system.ticketrepository.WeightTicketRepositoryIpml;
import org.weightbridge.system.weightticket.WeightTicket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/weightticket")
public class WeightTicketController {

    private static final Logger LOG = LoggerFactory.getLogger(WeightTicketController.class);

    public WeightTicketRepositoryIpml weightTicketRepository;

    public WeightTicketController(WeightTicketRepositoryIpml weightTicketRepository){
        this.weightTicketRepository = weightTicketRepository;
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/create")
//    void create(@RequestBody WeightTicket weightTicket){
//        LOG.info("Weight ticket details: {} ", weightTicket);
//        weightTicketRepository.save(weightTicket);
//    }

    @GetMapping("/getall")
    List<WeightTicket> findAll(){
        LOG.info("Return all weight tickets");
        return weightTicketRepository.findAll();
    }

    @GetMapping("/getbyid/{id}")
    public WeightTicket findWeightTicketById(@PathVariable UUID id){
        LOG.info("Return weight ticket by id {} ", id);
        Optional<WeightTicket> weightTicket = weightTicketRepository.findById(id);
        if(weightTicket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return weightTicket.get();
    }

    @GetMapping("/getbyticketnumber/{ticketNumber}")
    public WeightTicket findWeightTicketByTicketNumber(@PathVariable UUID ticketNumber){
        LOG.info("Return weight ticket by ticket number {} ", ticketNumber);
        Optional<WeightTicket> weightTicket = weightTicketRepository.findByTicketNumber(ticketNumber);
        if(weightTicket.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return weightTicket.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/firstweight/create")
    public void updateFirstWeightById(@RequestBody WeightTicket weightTicket){
        LOG.info("Update weight ticket {} with gross weight {} ");
            weightTicketRepository.save(weightTicket);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/update/secondweight/{ticketNumber}")
    public void updateSecondWeightById(@RequestBody WeightTicket weightTicket, @PathVariable UUID ticketNumber){
        LOG.info("Update weight ticket {} with gross weight {} ", ticketNumber);
        Optional<WeightTicket> existing = weightTicketRepository.findByTicketNumber(ticketNumber);
        if(existing.isPresent()){
            WeightTicket updatedTicket = new WeightTicket(existing.get().getId(),
                    existing.get().getTicketNumber(),
                    existing.get().getGrossWeight(),
                    existing.get().getTicketGrossWeightDateTime(),
                    weightTicket.getTareWeight(),
                    weightTicket.getTicketTareWeightDateTime(),
                    weightTicket.getNettWeight(),
                    existing.get().getVersion());
            LOG.info("Updated ticket {} ", updatedTicket);
            updatedTicket.setNettWeight(existing.get().getGrossWeight(), weightTicket.getTareWeight());
            updatedTicket.getNettWeight();
            weightTicketRepository.save(updatedTicket);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket {} not found. " + ticketNumber);
        }
    }
}
