package system.dev.marques.domain.dto.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteUserConfirmationDto {

    private String userName;

    private String userEmail;

    private String date;
}
