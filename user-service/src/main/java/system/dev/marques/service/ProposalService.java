package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.mapper.UserMapper;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final UserService userService;

    private final UserMapper mapper;

    public void proposal(ProposalRequest request, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        User user = userService.findUserById(userId);
        ProposalUserInfo proposalUserInfo = mapper.toProposalUserInfo(user, request);

    }
}
