package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;

    private String email;

    @CPF
    private String cpf;

    private String phoneNumber;

    private String password;

}
