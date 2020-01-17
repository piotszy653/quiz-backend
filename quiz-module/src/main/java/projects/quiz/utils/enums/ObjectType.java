package projects.quiz.utils.enums;

public enum ObjectType {

    QUIZ,
    QUESTION;

    public static ObjectType getType(String type, RuntimeException exceptionToThrow) {
        try {
            return ObjectType.valueOf(type);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw exceptionToThrow;
        }
    }
}
