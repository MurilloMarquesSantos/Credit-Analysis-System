package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.dev.marques.domain.Client;
import system.dev.marques.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
@Log4j2
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        log.info("Adding client {}", client);
        return new ResponseEntity<>(clientService.save(client), HttpStatus.CREATED);
    }


}
