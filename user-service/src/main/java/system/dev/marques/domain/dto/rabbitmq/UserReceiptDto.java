package system.dev.marques.domain.dto.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserReceiptDto {

    private long userId;

    private long proposalId;

    private String userName;

    private String userEmail;
}
