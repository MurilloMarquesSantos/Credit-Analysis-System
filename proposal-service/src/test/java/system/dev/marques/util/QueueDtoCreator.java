package system.dev.marques.util;

import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.*;
import system.dev.marques.domain.enums.ProposalStatus;

import java.time.LocalDateTime;

public class QueueDtoCreator {

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

    public static ProposalDto createProposalDto() {
        return ProposalDto.builder()
                .userId(1L)
                .cpf("12345678900")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.0)
                .requestedAmount(10000.0)
                .installments(12)
                .purpose("buy a car")
                .build();

    }

    public static AnalyzedDto createAnalyzedDto() {
        return AnalyzedDto.builder()
                .proposalId(1L)
                .cpf("123456789")
                .status(ProposalStatus.APPROVED)
                .rejectedReason("Rejected")
                .build();
    }

    public static ProposalCreditDto createProposalCreditDto() {
        return ProposalCreditDto.builder()
                .proposalId(1L)
                .cpf("123456789")
                .installments(70)
                .requestedAmount(5000D)
                .userIncome(500D)
                .build();
    }

    public static ProposalStatusEmailDto createProposalStatusEmailDto() {
        return ProposalStatusEmailDto.builder()
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .requestedAmount(5000D)
                .installments(70)
                .purpose("buy a house")
                .rejectedReason("rejected")
                .status(ProposalStatus.REJECTED)
                .build();
    }

    public static ApprovedProposalDto createApprovedProposalDto() {
        return ApprovedProposalDto.builder()
                .proposalId(1L)
                .userId(1L)
                .cpf("123456789")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.00)
                .requestedAmount(20000.00)
                .installments(24)
                .createdAt(LocalDateTime.now())
                .purpose("buy a car")
                .build();
    }
}
