package com.gyt.questionservice.business.messages;

public class Messages {
    public static class QuestionErrors{
        public static final String QuestionShouldBeExist = "questionShouldBeExist";
        public static final String UserAuthorityError = "userAuthorityError";
        public static final String TextOrImageUrlError = "textOrImageUrlError";
    }

    public static class OptionsErrors{
        public static final String MoreThanFiveAnswerErrors = "moreThanFiveAnswerErrors";
        public static final String CorrectAnswerNotFoundError = "correctAnswerNotFoundError";
        public static final String OptionsShouldBeExist  = "optionsShouldBeExist";
        public static final String TextOrImageUrlError = "textOrImageUrlError";

    }
}
