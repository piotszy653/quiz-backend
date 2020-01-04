package projects.core.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.NoSuchElementException;


@ControllerAdvice
@RestController
@RequiredArgsConstructor
@Slf4j
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Value("${spring.servlet.multipart.max-file-size}")
    String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDetails(messageSource.getMessage("file.max_size", new Object[]{maxFileSize}, null)), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDetails(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDetails(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        String[] splitMessage = e.getMessage().split(", ")[0].split(": ");
        String message = splitMessage.length > 1 ? splitMessage[1] : e.getMessage();
        HttpStatus status = "FORBIDDEN".equals(message) ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ErrorDetails(message),
                status
        );
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public final ResponseEntity handlePropertyReferenceException(PropertyReferenceException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorDetails(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public final ResponseEntity handleIOException(IOException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorDetails(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.toString());
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        status = checkForbidden(message, status);
        return new ResponseEntity<>(
                new ErrorDetails(
                        message
                ),
                status
        );
    }

    private HttpStatus checkForbidden(String message, HttpStatus status) {
        return message != null && message.contains("FORBIDDEN") ? HttpStatus.FORBIDDEN : status;
    }
}