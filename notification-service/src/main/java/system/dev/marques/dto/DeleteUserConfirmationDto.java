package system.dev.marques.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeleteUserConfirmationDto {

    private String userName;

    private String userEmail;

    private String date;
}
