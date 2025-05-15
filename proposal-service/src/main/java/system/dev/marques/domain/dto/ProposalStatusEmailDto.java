package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import system.dev.marques.domain.enums.ProposalStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalStatusEmailDto {

    private String userEmail;

    private String userName;

    private Double requestedAmount;

    private Integer installments;

    private String purpose;

    private String rejectedReason;

    private ProposalStatus status;
}
