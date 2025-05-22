package system.dev.marques.domain.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequestGoogle {

    @NotBlank(message = "The name must not be empty")
    private String name;

    @CPF
    private String cpf;

    @NotBlank(message = "The phone number must not be empty")
    private String phoneNumber;

    @NotNull(message = "Income must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Income must be greater than zero")
    private Double income;

    @NotBlank(message = "The password must not be empty")
    private String password;
}
