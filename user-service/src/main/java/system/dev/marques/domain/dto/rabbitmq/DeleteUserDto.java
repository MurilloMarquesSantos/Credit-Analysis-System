package system.dev.marques.domain.dto.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeleteUserDto {

    private long userId;

    private List<String> adminEmails;

    private String userEmail;

    private String reason;
}
