package system.dev.marques.util;

import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;

import java.time.LocalDateTime;

public class HistoryResponseCreator {

    public static ProposalHistoryResponse createProposalHistoryResponse() {
        return ProposalHistoryResponse.builder()
                .proposalId(1L)
                .cpf("123456789")
                .requestedAmount(5000D)
                .status("APPROVED")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
