package system.dev.marques.util;

import system.dev.marques.dto.AnalyzedDto;
import system.dev.marques.dto.ProposalCreditDto;
import system.dev.marques.enums.ProposalStatus;

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

    public static AnalyzedDto createAnalyzedDto() {
        return AnalyzedDto.builder()
                .proposalId(1L)
                .cpf("123456789")
                .rejectedReason("Rejected")
                .status(ProposalStatus.REJECTED)
                .build();
    }
}
