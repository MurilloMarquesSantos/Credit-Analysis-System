package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalDto {

    private Long userId;

    private String cpf;

    private String userEmail;

    private String userName;

    private Double userIncome;

    private Double requestedAmount;

    private Integer installments;

    private String purpose;
}
