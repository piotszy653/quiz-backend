package projects.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import projects.core.service.quiz.CoreResultService;
import projects.quiz.dto.result.ResultCreateDto;
import projects.quiz.model.Result;
import projects.quiz.service.ResultService;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/results")
public class ResultController {

    private final CoreResultService coreResultService;

    private final ResultService resultService;

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Result getByUuid(@PathVariable String uuid) {
        return resultService.getByUuid(UUID.fromString(uuid));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Result> getByUser() {
        return coreResultService.getByUser();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result create(@Valid @RequestBody ResultCreateDto resultCreateDto) {
        return coreResultService.create(resultCreateDto);
    }
}
