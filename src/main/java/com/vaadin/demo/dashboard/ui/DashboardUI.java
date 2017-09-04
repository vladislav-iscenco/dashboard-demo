package com.vaadin.demo.dashboard.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;

/**
 * Created by Ronen on 9/3/2017.
 */
@Theme("dashboard")
@Widgetset("com.vaadin.demo.dashboard.DashboardWidgetSet")
@Title("Dashboard")
@SpringUI(path = "/")
public class DashboardUI extends AbstractSideBarUI {

    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    private final DashboardSideBar sideBar;

    @Autowired
    public DashboardUI(SpringViewProvider viewProvider, DashboardSideBar sideBar) {
        super(viewProvider);
        this.sideBar = sideBar;
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
    }

    @Override
    protected AbstractSideBar getSideBar() {
        DashboardEventBus.register(sideBar);
        return sideBar;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((com.vaadin.demo.dashboard.ui.DashboardUI) getCurrent()).dashboardEventbus;
    }
}
