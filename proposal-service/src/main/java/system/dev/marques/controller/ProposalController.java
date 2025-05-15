package system.dev.marques.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.service.ProposalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service")
public class ProposalController {

    private final ProposalService proposalService;

    @GetMapping("/history/{id}")
    public ResponseEntity<List<ProposalHistoryResponse>> getProposalHistory(@PathVariable Long id) {
        return ResponseEntity.ok(proposalService.getProposalHistory(id));
    }
}
