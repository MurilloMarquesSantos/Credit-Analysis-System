package system.dev.marques.domain.dto.proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalRequest {

    private Double requestedAmount;

    private Integer installments;

    private String purpose;
}
