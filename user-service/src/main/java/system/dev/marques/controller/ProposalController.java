package system.dev.marques.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.service.ProposalService;

import java.security.Principal;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping("/send-proposal")
    public ResponseEntity<String> proposal(@RequestBody @Valid ProposalRequest request, Principal principal) throws BadRequestException {
        return ResponseEntity.ok(proposalService.propose(request, principal));
    }
}
