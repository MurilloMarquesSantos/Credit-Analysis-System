package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final UserService userService;

    private final UserMapper mapper;

    private final ProducerService producerService;

    private final UserRepository userRepository;

    public String propose(ProposalRequest request, Principal principal) throws BadRequestException {
        Long userId = Long.valueOf(principal.getName());
        User user = userService.findUserById(userId);

        if (hasSubmittedProposalInLast24Hours(user)) {
            long minutesLeft = 1440 - Duration.between(user.getLastProposalAt(), LocalDateTime.now()).toMinutes();
            long hours = minutesLeft / 60;
            long minutes = minutesLeft % 60;

            throw new BadRequestException(
                    String.format(
                            "You already submitted a proposal in the last 24 hours." +
                                    " Try again in %d hour(s) and %d minute(s).", hours, minutes)
            );
        }

        ProposalUserInfo proposalUserInfo = mapper.toProposalUserInfo(user, request);
        producerService.sendProposal(proposalUserInfo);

        user.setLastProposalAt(LocalDateTime.now());
        userRepository.save(user);

        return "Proposal sent for review. Please monitor your email inbox for further updates.";

    }

    private boolean hasSubmittedProposalInLast24Hours(User user) {
        LocalDateTime submittedAt = user.getLastProposalAt();
        return submittedAt != null && Duration.between(submittedAt, LocalDateTime.now()).toHours() < 24;
    }


}
