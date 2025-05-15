package system.dev.marques.domain.dto.proposal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProposalRequest {

    @NotNull(message = "Request amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Request amount must be greater than zero")
    private Double requestedAmount;

    @NotNull(message = "Installment amount must not be null")
    @Min(value = 1, message = "Installment request must be at least one")
    private Integer installments;

    @NotBlank(message = "Purpose of the request must be provided")
    private String purpose;
}
