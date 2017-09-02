package com.vaadin.demo.dashboard.component;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.domain.DashboardNotification;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;

/**
 * Created by Ronen on 9/2/2017.
 */
public final class NotificationsButton extends Button {
    private static final String STYLE_UNREAD = "unread";
    public static final String ID = "dashboard-notifications";

    private Window notificationsWindow;

    public NotificationsButton() {
        setIcon(VaadinIcons.BELL);
        setId(ID);
        addStyleName("notifications");
        addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        DashboardEventBus.register(this);

        addClickListener(this::openNotificationsPopup);
    }

    @Subscribe
    public void updateNotificationsCount(
            final DashboardEvent.NotificationsCountUpdatedEvent event) {
        setUnreadCount(DashboardUI.getDataProvider()
                .getUnreadNotificationsCount());
    }

    public void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));

        String description = "Notifications";
        if (count > 0) {
            addStyleName(STYLE_UNREAD);
            description += " (" + count + " unread)";
        } else {
            removeStyleName(STYLE_UNREAD);
        }
        setDescription(description);
    }

    private void openNotificationsPopup(final ClickEvent event) {
        VerticalLayout notificationsLayout = new VerticalLayout();

        Label title = new Label("Notifications");
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        notificationsLayout.addComponent(title);

        Collection<DashboardNotification> notifications =
                DashboardUI.getDataProvider().getNotifications();
        DashboardEventBus.post(new DashboardEvent.NotificationsCountUpdatedEvent());

        notifications.forEach(notification -> {
            VerticalLayout notificationLayout = new VerticalLayout();
            notificationLayout.setMargin(false);
            notificationLayout.setSpacing(false);
            notificationLayout.addStyleName("notification-item");

            Label titleLabel = new Label(notification.getFirstName() + " "
                    + notification.getLastName() + " "
                    + notification.getAction());
            titleLabel.addStyleName("notification-title");

            Label timeLabel = new Label(notification.getPrettyTime());
            timeLabel.addStyleName("notification-time");

            Label contentLabel = new Label(notification.getContent());
            contentLabel.addStyleName("notification-content");

            notificationLayout.addComponents(titleLabel, timeLabel,
                    contentLabel);
            notificationsLayout.addComponent(notificationLayout);
        });

        Button showAll = new Button("View All Notifications",
                (ClickListener) event1 -> Notification.show("Not implemented in this demo"));
        showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        showAll.addStyleName(ValoTheme.BUTTON_SMALL);

        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth("100%");
        footer.setSpacing(false);
        footer.addComponent(showAll);
        footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
        notificationsLayout.addComponent(footer);

        if (notificationsWindow == null) {
            notificationsWindow = new Window();
            notificationsWindow.setWidth(300.0f, Unit.PIXELS);
            notificationsWindow.addStyleName("notifications");
            notificationsWindow.setClosable(false);
            notificationsWindow.setResizable(false);
            notificationsWindow.setDraggable(false);
            notificationsWindow.addShortcutListener(getWindowCloseListener(notificationsWindow));
            notificationsWindow.setContent(notificationsLayout);
        }

        if (!notificationsWindow.isAttached()) {
            notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            getUI().addWindow(notificationsWindow);
            notificationsWindow.focus();
        } else {
            notificationsWindow.close();
        }
    }

    private ShortcutListener getWindowCloseListener(Window window) {
        return new ShortcutListener("close", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                window.close();
            }
        };
    }
}
