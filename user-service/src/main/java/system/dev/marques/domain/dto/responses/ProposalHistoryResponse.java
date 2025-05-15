package system.dev.marques.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalHistoryResponse {

    private String cpf;

    private Double requestedAmount;

    private String status;

    private LocalDateTime createdAt;
}
