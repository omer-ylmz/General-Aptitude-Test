package com.gyt.questionservice.business.messages;

public class Messages {
    public static class QuestionErrors{
        public static final String QuestionShouldBeExist = "questionShouldBeExist";
        public static final String UserAuthorityError = "userAuthorityError";
        public static final String TextOrImageUrlError = "textOrImageUrlError";
        public static final String QuestionUpdateRestrictedDueToExamStatus = "questionUpdateRestrictedDueToExamStatus";
    }

    public static class OptionsErrors{
        public static final String MoreThanFiveAnswerErrors = "moreThanFiveAnswerErrors";
        public static final String CorrectAnswerNotFoundError = "correctAnswerNotFoundError";
        public static final String OptionsShouldBeExist  = "optionsShouldBeExist";
        public static final String TextOrImageUrlError = "textOrImageUrlError";
        public static final String AtLeastOneCorrectOptionRule = "atLeastOneCorrectOptionRule";
        public static final String AtLeastTwoAnswerCheckErrors = "atLeastTwoAnswerCheckErrors";

    }
}


// TODO: 1.08.2024 türkçe karakter izni bakılacak