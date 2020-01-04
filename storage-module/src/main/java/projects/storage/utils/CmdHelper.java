package projects.storage.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CmdHelper {

    private MessageSource messageSource;

    public Process execute(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("cmd.io_exception", null, null));
        }
    }

    public void wait(Process proc) {
        try {
            if (proc != null)
                proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(messageSource.getMessage("cmd.interrupted_exception", null, null));
        } finally {
            if (proc != null)
                proc.destroy();
        }
    }

}
