package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
    public String proposal(@RequestBody ProposalRequest request, Principal principal) throws BadRequestException {
        proposalService.propose(request, principal);
        return "Proposal sent for review. Please monitor your email inbox for further updates.";
    }
}
