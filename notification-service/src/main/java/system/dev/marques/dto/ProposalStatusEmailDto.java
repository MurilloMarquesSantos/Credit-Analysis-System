package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String status;
}
