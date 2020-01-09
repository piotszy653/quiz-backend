package projects.quiz.utils.enums;

public enum QuestionType {
    TRUE_FALSE,
    OPEN,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE;

    public boolean isTestType(){
        return this.equals(SINGLE_CHOICE) || this.equals(MULTIPLE_CHOICE);
    }
}
