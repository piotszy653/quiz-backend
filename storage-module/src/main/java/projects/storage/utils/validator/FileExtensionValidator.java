package projects.storage.utils.validator;

import projects.storage.utils.tika.TikaHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
public class FileExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {

    @NonNull
    private MessageSource messageSource;

    private List<String> extensions;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        extensions = Arrays.asList(constraintAnnotation.extensions());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
            String extension = TikaHelper.getExtension(file);
            if(!extensions.contains(extension))
                throw new IllegalArgumentException(messageSource.getMessage("file.wrong_extension.allowed_extensions", new Object[]{extensions.toString()}, null));
            return true;
    }
}
