package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApprovedProposalDto {

    private Long proposalId;

    private Long userId;

    private String cpf;

    private String userEmail;

    private String userName;

    private Double userIncome;

    private Double requestedAmount;

    private Integer installments;

    private LocalDateTime createdAt;

    private String purpose;

}
