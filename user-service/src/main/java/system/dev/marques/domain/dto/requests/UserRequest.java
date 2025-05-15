package system.dev.marques.domain.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "The name must not be empty")
    private String name;

    @NotBlank(message = "The email must not be empty")
    private String email;

    @CPF
    private String cpf;

    @NotBlank(message = "The phone number must not be empty")
    private String phoneNumber;

    @NotBlank(message = "The password must not be empty")
    private String password;

}
