package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestGoogle {

    private String name;

    @CPF
    private String cpf;

    private String phoneNumber;

    private Double income;

    private String password;
}
