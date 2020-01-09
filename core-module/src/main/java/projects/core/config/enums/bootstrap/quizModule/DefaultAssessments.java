package projects.core.config.enums.bootstrap.quizModule;

import lombok.AllArgsConstructor;
import projects.core.config.enums.roles.RolesEnum;


@AllArgsConstructor
public enum DefaultAssessments {
    TF_ASSESSMENT("True false assessment without points borders", 1.0f, -0.5f, null, null),
    MULTIPLE_CHOICE_ASSESSMENT("Multiple choice assessment", 1.0f, -0.5f, null, 0.0f),
    SINGLE_CHOICE_ASSESSMENT("Single choice assessment", 1.0f, -0.5f, null, null);

    public final String name;
    public final Float correctRate;
    public final Float incorrectRate;
    public final Float maxPoints;
    public final Float minPoints;
}

