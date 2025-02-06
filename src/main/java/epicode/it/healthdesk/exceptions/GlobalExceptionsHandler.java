package epicode.it.healthdesk.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ErrorMessage> handleEntityExistsException(EntityExistsException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<ErrorMessage> handleSecurityException(SecurityException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            if (fieldName.contains(".")) {
                fieldName = fieldName.substring(fieldName.lastIndexOf('.') + 1);
            }
            errors.put(fieldName, violation.getMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
