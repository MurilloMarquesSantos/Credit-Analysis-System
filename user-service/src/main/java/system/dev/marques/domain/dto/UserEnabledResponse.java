package system.dev.marques.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEnabledResponse {
    private Long id;

    private String name;

    private String email;

    private String cpf;

    private String phoneNumber;

    private Double income;

    private String password;
}
