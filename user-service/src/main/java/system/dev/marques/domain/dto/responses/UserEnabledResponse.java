package system.dev.marques.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEnabledResponse {
    private Long id;

    private String name;

    private String email;

    private String cpf;

    private String phoneNumber;

    private Double income;

    private String password;
}
