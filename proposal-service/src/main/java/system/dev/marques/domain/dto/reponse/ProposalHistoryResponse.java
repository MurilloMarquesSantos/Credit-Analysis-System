package system.dev.marques.domain.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import system.dev.marques.domain.enums.ProposalStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalHistoryResponse {

    private String cpf;

    private Double requestedAmount;

    private ProposalStatus status;

    private LocalDateTime createdAt;

}
