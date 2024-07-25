package com.gyt.managementservice.business.messages;

public class Messages {

    public static class AuthErrors {
        public static final String AuthenticationFailed = "authenticationFailed";

    }

    public static class UserErrors{
        public static final String UserShouldBeExists = "userShouldBeExists";
        public static final String UpdateAuthorityError = "updateAuthorityError";
    }

    public static class RoleErrors{
        public static final String RoleShouldBeExists = "roleShouldBeExists";
    }
}
