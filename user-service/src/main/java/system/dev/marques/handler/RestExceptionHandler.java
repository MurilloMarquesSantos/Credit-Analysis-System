package system.dev.marques.handler;

import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import system.dev.marques.exception.*;
import system.dev.marques.exception.custom.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss");

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String rootMessage = Optional.ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                        .status(HttpStatus.CONFLICT)
                        .message("Could not complete the operation due to a data conflict")
                        .details("Data integrity violation. Possible constraint error (e.g. unique, foreign key).")
                        .developerMessage(ex.getClass().getName())
                        .fields(null)
                        .fieldsMessages(rootMessage)
                        .path(request.getDescription(false))
                        .build(), HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionDetails> handleUnavailableService(ServiceUnavailableException ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("An error occurred while trying to fetch history")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionDetails> handleInvalidTokenEx(InvalidTokenException ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.GONE)
                .message("Link expired or invalid")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.GONE
        );
    }

    @ExceptionHandler(EmailValidationException.class)
    public ResponseEntity<ExceptionDetails> handleEmailValidation(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed for email")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EmailExistingException.class)
    public ResponseEntity<ExceptionDetails> handleEmailExistingEx(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed for email")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ExceptionDetails> handlePasswordValidation(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed for password")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status(HttpStatus.BAD_REQUEST)
                .message("An unexpected error occurred")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .path(request.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, WebRequest request) {
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
            MethodArgumentNotValidException ex, @Nullable HttpHeaders headers, @NonNull HttpStatusCode status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String field = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Validation failed for field(s): " + field)
                        .details("Check the field(s) error")
                        .developerMessage(ex.getClass().getName())
                        .fields(field)
                        .fieldsMessages(fieldMessage)
                        .path(request.getDescription(false))
                        .build(), HttpStatus.BAD_REQUEST
        );
    }
}
