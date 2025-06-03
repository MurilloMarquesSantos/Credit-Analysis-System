package system.dev.marques.util;

import system.dev.marques.domain.dto.proposal.ProposalRequest;

public class RequestCreator {

    public static ProposalRequest createProposalRequest() {
        return ProposalRequest.builder()
                .requestedAmount(5000D)
                .installments(60)
                .purpose("Buy a car").build();
    }

    public static ProposalRequest createInvalidProposalRequest() {
        return ProposalRequest.builder().build();
    }

}
