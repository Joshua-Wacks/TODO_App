package com.bbd.toDoApp.Frontend;

import com.bbd.toDoApp.model.User;

public class StateObject {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        StateObject.loggedInUser = loggedInUser;
    }
}
