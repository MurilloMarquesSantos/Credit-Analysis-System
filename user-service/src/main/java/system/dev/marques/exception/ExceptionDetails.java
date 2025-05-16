package system.dev.marques.exception;


import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
public class ExceptionDetails {

    private String timestamp;

    private String message;

    private String details;

    private String developerMessage;

    private HttpStatus status;

    private String path;
}
