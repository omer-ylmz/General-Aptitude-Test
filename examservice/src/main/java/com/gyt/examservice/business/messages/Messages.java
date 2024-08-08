package com.gyt.examservice.business.messages;

public class Messages {
    public static class ExamErrors{
        public static final String StartDateInPast = "startDateInPast";
        public static final String EndDateBeforeStartDate = "endDateBeforeStartDate";
        public static final String DuplicateQuestionsNotAllowed = "duplicateQuestionsNotAllowed";
        public static final String ExamShouldBeExist = "examShouldBeExist";
        public static final String UserAuthorityError = "userAuthorityError";
        public static final String ExamCannotBeModifiedBecauseItHasStarted = "examCannotBeModifiedBecauseItHasStarted";
        public static final String QuestionNotFoundInExam = "questionNotFoundInExam";
    }
}
