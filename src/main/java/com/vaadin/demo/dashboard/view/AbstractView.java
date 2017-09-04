package com.vaadin.demo.dashboard.view;

import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * Created by Ronen on 9/4/2017.
 */
public abstract class AbstractView extends Panel implements View {

    private String viewPath;
    private String viewName;
    private String viewTitle;
    private Resource viewIcon;

    protected VerticalLayout contentWrapper;

    public AbstractView() {
        super();
        setSizeFull();
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        VaadinSession.getCurrent().setLocale(Locale.US);
        DashboardEventBus.register(this);
        setLocale(Locale.US);
    }

    @PostConstruct
    protected void createContentWrapper(){
        Component content = createContent();
        this.contentWrapper = new VerticalLayout(content);
        this.contentWrapper.setSpacing(false);
        // All the open sub-windows should be closed
        // whenever the root layout gets clicked.
        this.contentWrapper.addLayoutClickListener(event ->
                DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent()));
        Responsive.makeResponsive(this.contentWrapper);
        setContent(this.contentWrapper);
    }

    protected abstract Component createContent();

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewTitle() {
        return viewTitle;
    }

    public void setViewTitle(String viewTitle) {
        this.viewTitle = viewTitle;
    }

    public Resource getViewIcon() {
        return viewIcon;
    }

    public void setViewIcon(Resource viewIcon) {
        this.viewIcon = viewIcon;
    }
}
