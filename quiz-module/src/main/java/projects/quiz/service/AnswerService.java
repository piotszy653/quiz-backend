package projects.quiz.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import projects.quiz.dto.answer.AnswerDto;
import projects.quiz.model.Answer;
import projects.quiz.repository.AnswerRepository;
import projects.storage.model.FileData;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Validated
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final MessageSource messageSource;

    public Answer getById(Long id){
        return answerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage("answer.not_found.id", new Object[]{id}, null)));
    }

    public Answer getByUuid(UUID uuid){
        return answerRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(notFoundByUuidMessage(uuid)));
    }

    public LinkedHashSet<Answer> getAllByUuids(Collection<String> uuidStringSet){
        Iterable<UUID> uuids = uuidStringSet.stream().map(UUID::fromString).collect(Collectors.toSet());
        return answerRepository.findAllByUuid(uuids);
    }

    public LinkedHashSet<Answer> getByOwnerUuid(UUID ownerUuid) {
        return answerRepository.findAllByOwnerUuid(ownerUuid);
    }

    @Transactional
    public Answer create(AnswerDto answerDto, UUID ownerUuid, FileData imageData){
        return save(new Answer(answerDto, ownerUuid, imageData));
    }

    @Transactional
    public LinkedHashSet<Answer> createAll(Collection<AnswerDto> dtoSet, UUID ownerUuid, HashMap<String, FileData> imagesData){
        return dtoSet.stream()
                .map(dto ->
                        create(
                                dto,
                                ownerUuid,
                                imagesData.get(dto.getImageUuid())
                        )
                )
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public Answer save(@Valid Answer answer) {
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer update(UUID answerUuid, AnswerDto dto, FileData imageData){

        return answerRepository.findByUuid(answerUuid)
                .map(answer -> {
                    answer.setAnswer(dto.getAnswer() != null ? dto.getAnswer() : answer.getAnswer());
                    answer.setImageData(imageData != null ? imageData : answer.getImageData());
                    return save(answer);
                })
                .orElseThrow(() -> new NoSuchElementException(notFoundByUuidMessage(answerUuid)));
    }

    @Transactional
    public void delete(UUID uuid) {
        answerRepository.delete(getByUuid(uuid));
    }

    @Transactional
    public void deleteAll(Collection<Answer> answers) {
        answerRepository.deleteAll(answers);
    }

    @Transactional
    public void deleteAllByOwnerUuid(UUID ownerUuid) {
        answerRepository.deleteAllByOwnerUuid(ownerUuid);
    }

    private String notFoundByUuidMessage(UUID uuid){
        return messageSource.getMessage("answer.not_found.uuid", new Object[]{uuid}, null);
    }



}
