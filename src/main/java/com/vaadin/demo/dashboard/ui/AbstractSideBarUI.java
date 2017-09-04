package com.vaadin.demo.dashboard.ui;

import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.AbstractView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.navigator.MNavigator;

/**
 * Created by Ronen on 9/3/2017.
 */
public abstract class AbstractSideBarUI extends AbstractUI {

    private Label titleLabel = new Label();
    private VerticalLayout rightContainer;

    public VerticalLayout getRightContainer() {
        return rightContainer;
    }

    @Autowired
    public AbstractSideBarUI(SpringViewProvider viewProvider) {
        super(viewProvider);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setErrorHandler();
        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        final VerticalLayout viewContainer = new MVerticalLayout()
                .withFullSize().withMargin(false).withSpacing(false);

        rightContainer = new MVerticalLayout()
                .with(
                        buildHeader(),
                        buildSparklines(),
                        viewContainer
                )
                .withFullSize().withMargin(false).withSpacing(false)
                .withExpand(viewContainer, 1.0F)
                .withStyleName("view-content");

        buildNavigator(viewContainer);

        final MHorizontalLayout rootLayout = new MHorizontalLayout()
                .with(getSideBar(), rightContainer)
                .withFullSize().withMargin(false).withSpacing(false)
                .withExpand(rightContainer, 1.0F);
        setContent(rootLayout);
    }

    private void buildNavigator(VerticalLayout viewContainer) {
        final Navigator navigator = new MNavigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                titleLabel.setCaption(viewChangeEvent.getViewName());
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                View newView = event.getNewView();
                if (newView instanceof AbstractView) {
                    AbstractView view = (AbstractView) newView;
                    titleLabel.setCaption(view.getViewTitle());
                    titleLabel.setIcon(view.getViewIcon());
                    getPage().setTitle(view.getViewTitle());

                }
            }
        });
        setNavigator(navigator);
    }

    private HorizontalLayout buildHeader() {
        titleLabel = new Label();
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        return new MHorizontalLayout(titleLabel)
                .withMargin(new MarginInfo(
                        false, true, false, true))
                .withWidth(100, Unit.PERCENTAGE)
                .withHeight(42, Unit.PIXELS)
                .withStyleName("viewheader");
    }

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth(100, Unit.PERCENTAGE);
        Responsive.makeResponsive(sparks);
        return sparks;
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