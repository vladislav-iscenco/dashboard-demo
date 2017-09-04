package com.vaadin.demo.dashboard.event;

import com.vaadin.demo.dashboard.view.AbstractView;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
        private String name;

        public ProfileUpdatedEvent(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static final class PostViewChangeEvent {
        private final AbstractView view;

        public PostViewChangeEvent(final AbstractView view) {
            this.view = view;
        }

        public AbstractView getView() {
            return view;
        }
    }

}
