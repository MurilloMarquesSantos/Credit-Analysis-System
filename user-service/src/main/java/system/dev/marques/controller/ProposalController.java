package system.dev.marques.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Proposal", description = "Operation to submit a proposal")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping("/send-proposal")
    @Operation(summary = "Submit Proposal", description = "Submit proposal requisition.",
            tags = {"Proposal"})
    @ApiResponse(responseCode = "200", description = "Request processed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized – token missing or invalid",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Unauthorized user")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "User not found")))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "Already proposed in the last 'x' hours. Try again in 'y' hours")))
    public ResponseEntity<String> proposal(@RequestBody @Valid ProposalRequest request, Principal principal)
            throws BadRequestException {
        return ResponseEntity.ok(proposalService.propose(request, principal));
    }
}
