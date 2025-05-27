package system.dev.marques.domain.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteForm {

    @NotBlank(message = "This field is mandatory")
    private String reason;
}
