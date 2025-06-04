package system.dev.marques.util;

import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.domain.enums.ProposalStatus;

import java.time.LocalDateTime;

public class ProposalCreator {

    public static Proposal createProposal() {
        return Proposal.builder()
                .userId(1L)
                .cpf("12345678900")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.0)
                .requestedAmount(10000.0)
                .installments(12)
                .purpose("buy a car")
                .status(ProposalStatus.PENDING)
                .build();
    }

    public static ProposalHistoryResponse createProposalHistoryResponse() {
        return ProposalHistoryResponse.builder()
                .proposalId(1L)
                .createdAt(LocalDateTime.now())
                .cpf("0123456789")
                .requestedAmount(5000D)
                .status(ProposalStatus.APPROVED)
                .build();

    }

    public static Proposal createSavedProposal() {
        return Proposal.builder()
                .id(1L)
                .userId(1L)
                .cpf("12345678900")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.0)
                .requestedAmount(10000.0)
                .installments(12)
                .purpose("buy a car")
                .status(ProposalStatus.PENDING)
                .build();
    }
}
