package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import system.dev.marques.enums.ProposalStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalyzedDto {

    private Long proposalId;

    private String cpf;

    private ProposalStatus status;

    private String rejectedReason;

}
