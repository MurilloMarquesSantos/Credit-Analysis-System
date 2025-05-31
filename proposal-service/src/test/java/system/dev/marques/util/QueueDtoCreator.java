package system.dev.marques.util;

import system.dev.marques.domain.dto.ApprovedProposalDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;
import system.dev.marques.domain.enums.ProposalStatus;

import java.time.LocalDateTime;

public class QueueDtoCreator {

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
