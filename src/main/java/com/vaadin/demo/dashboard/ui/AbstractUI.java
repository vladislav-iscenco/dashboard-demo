package com.vaadin.demo.dashboard.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by Ronen on 9/3/2017.
 */
public abstract class AbstractUI extends UI {

    private static Logger logger = LoggerFactory.getLogger(AbstractUI.class);

    final SpringViewProvider viewProvider;

    @Autowired
    public AbstractUI(SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setErrorHandler();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        setContent(rootLayout);

        final VerticalLayout viewContainer = new MVerticalLayout()
                .withStyleName("view-content")
                .withFullSize();

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(viewContainer);
        rootLayout.setExpandRatio(viewContainer, 1.0f);


        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event ->
                        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent()));
    }


    protected abstract AbstractSideBar getSideBar();

    private class ErrorView extends VerticalLayout implements View {
        private Label message;

        ErrorView() {
            setMargin(true);
            addComponent(message = new Label());
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("No such view: %s", event.getViewName()));
        }
    }

    @Subscribe
    public void closeOpenWindows(final DashboardEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    protected void setErrorHandler() {
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
