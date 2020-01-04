package projects.user.security.exceptions.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import projects.user.security.exceptions.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public interface SecurityExceptionHandler {

    default void handleException(HttpServletResponse response, Exception ex, HttpStatus status)
            throws IOException {
        ResponseCode responseCode = new ResponseCode(ex.getMessage());
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, responseCode);
        out.flush();
    }
}
