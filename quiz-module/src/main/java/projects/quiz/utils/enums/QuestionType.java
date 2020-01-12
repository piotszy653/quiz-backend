package projects.quiz.utils.enums;

public enum QuestionType {
    TRUE_FALSE,
    OPEN,
    TEST;

    public static QuestionType getType(String type, RuntimeException exceptionToThrow) {
        try{
            return QuestionType.valueOf(type);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            throw exceptionToThrow;
        }
    }

}
