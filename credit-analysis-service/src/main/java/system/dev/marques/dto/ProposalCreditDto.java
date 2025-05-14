package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalCreditDto {

    private Long proposalId;

    private String cpf;

    private Double userIncome;

    private Double requestedAmount;

    private int installments;

}
