package system.dev.marques.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import system.dev.marques.exception.ExceptionDetails;
import system.dev.marques.exception.InvalidTokenException;
import system.dev.marques.exception.ValidationExceptionDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


//todo create a handler for DataIntegrityException

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss");

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionDetails> handleInvalidTokenEx(InvalidTokenException ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.UNAUTHORIZED)
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.BAD_REQUEST)
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status((HttpStatus) statusCode)
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(exceptionDetails, headers, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String field = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                        .status(HttpStatus.BAD_REQUEST)
                        .details("Check the field(s) error")
                        .developerMessage(ex.getClass().getName())
                        .fields(field)
                        .fieldsMessages(fieldMessage)
                        .path(request.getDescription(false))
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
