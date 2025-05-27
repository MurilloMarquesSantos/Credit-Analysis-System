package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserReceiptDto {

    private long userId;

    private long proposalId;

    private String userName;

    private String userEmail;
}
