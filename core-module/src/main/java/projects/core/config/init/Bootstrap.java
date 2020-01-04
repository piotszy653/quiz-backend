package projects.core.config.init;

import projects.core.service.init.BootstrapService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Component
@Slf4j
public class Bootstrap {

    BootstrapService bootstrapService;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        try {
            bootstrapService.setup();
            log.info("Bootstrap success");
        } catch (Exception e) {
            log.error("Bootstrap failed," + e);
            e.printStackTrace();
            throw e;
        }
    }

}

