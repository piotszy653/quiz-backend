package projects.storage.utils.validator;

import projects.storage.service.FileDataService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class FileDataExistsValidator implements ConstraintValidator<FileDataExists, Long> {

    private FileDataService fileDataService;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return (id == null) || fileDataService.existsById(id);
    }
}
