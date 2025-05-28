package system.dev.marques.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAdminResponse {

    private Long id;

    private String name;

    private String email;

    private String cpf;

    private String phoneNumber;

    private Double income;

    private String password;

    private Set<String> roles;

    private boolean valid;

    private LocalDateTime lastProposalAt;
}
