package system.dev.marques.domain.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEnableRequest {

    @NotNull(message = "Income must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Income must be greater than zero")
    private Double income;
}
