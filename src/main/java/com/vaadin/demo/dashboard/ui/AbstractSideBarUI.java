package com.vaadin.demo.dashboard.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.viritin.navigator.MNavigator;

/**
 * Created by Ronen on 9/3/2017.
 */
public abstract class AbstractSideBarUI extends AbstractUI {

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        rootLayout.setSpacing(false);

        setContent(rootLayout);

        final VerticalLayout rightContainer = new VerticalLayout();
        rightContainer.setSizeFull();
        rightContainer.setMargin(false);
        rightContainer.setSpacing(false);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(42, Unit.PIXELS);
        topLayout.setWidth(100, Unit.PERCENTAGE);
        topLayout.setMargin(new MarginInfo(false, true, false, true));

        Button titleButton = new Button();
        titleButton.setCaptionAsHtml(true);
        titleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        topLayout.addComponent(titleButton);
        topLayout.setComponentAlignment(titleButton, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(titleButton, 0);

        rightContainer.addComponent(topLayout);

        final VerticalLayout viewContainer = new VerticalLayout();
        viewContainer.setSizeFull();
        viewContainer.setMargin(false);
        viewContainer.setSpacing(false);

        rightContainer.addComponent(viewContainer);
        rightContainer.setExpandRatio(viewContainer, 1.0F);

        final Navigator navigator = new MNavigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        ViewChangeListener viewChangeListener = new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        };

        titleButton.addClickListener(clickEvent -> {
            navigator.navigateTo(navigator.getState());
        });

        navigator.addViewChangeListener(viewChangeListener);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(rightContainer);
        rootLayout.setExpandRatio(rightContainer, 1.0f);
    }


    protected abstract AbstractSideBar getSideBar();

    private class ErrorView extends VerticalLayout implements View {

        private Label message;

        ErrorView() {
            setMargin(true);
            message = new Label();
            addComponent(message);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("No such view: %s", event.getViewName()));
        }
    }
}