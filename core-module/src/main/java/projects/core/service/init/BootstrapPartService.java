package projects.core.service.init;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.core.config.init.BootstrapPartName;
import projects.core.dto.init.CreateBootstrapPartDto;
import projects.core.model.init.BootstrapPart;
import projects.core.repository.init.BootstrapPartRepository;

import javax.validation.Valid;

@AllArgsConstructor
@Validated
@Service
@Transactional(readOnly = true)
@Slf4j
public class BootstrapPartService {

    BootstrapPartRepository bootstrapPartRepository;

    void create(BootstrapPartName key, Runnable runnable) {
        String line = "processing " + key + " ->";
        if (!existsByKey(key)) {
            runnable.run();
            CreateBootstrapPartDto createBootstrapPartDto = new CreateBootstrapPartDto(key);
            create(createBootstrapPartDto);
            line += " creating";
        } else {
            line += " already in db";
        }
        log.info(line);
    }

    public boolean existsByKey(BootstrapPartName key) {
        return bootstrapPartRepository.existsByKey(key);
    }

    @Transactional
    public BootstrapPart create(@Valid CreateBootstrapPartDto dto) {
        BootstrapPart bootStrapPart = new BootstrapPart(dto.getKey());
        return bootstrapPartRepository.save(bootStrapPart);
    }
}
