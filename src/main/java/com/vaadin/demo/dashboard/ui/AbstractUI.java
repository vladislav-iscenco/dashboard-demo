package com.vaadin.demo.dashboard.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Created by Ronen on 9/3/2017.
 */
public abstract class AbstractUI extends UI {

    protected static Logger logger = LoggerFactory.getLogger(AbstractUI.class);

    @Override
    protected void init(final VaadinRequest request) {
        setErrorHandler();
        setLocale(Locale.US);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        getNavigator().navigateTo("");

        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event ->
                        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent()));
    }

    @Subscribe
    public void closeOpenWindows(final DashboardEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }


    private void setErrorHandler() {
        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                super.error(event);
                // Send an email telling about the error
                String cause = "";
                for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {
                    if (t.getCause() == null) {
                        cause += t.getClass().getName();
                    }
                }
                logger.error(event.getThrowable().getMessage(), event.getThrowable().getCause());
                Notification.show(cause, Notification.Type.ERROR_MESSAGE);
            }
        });
    }

}
