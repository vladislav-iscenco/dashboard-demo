package com.vaadin.demo.dashboard.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.viritin.label.MLabel;

/**
 * Created by Ronen on 9/3/2017.
 */
@Theme("dashboard")
@Widgetset("com.vaadin.demo.dashboard.DashboardWidgetSet")
@Title("Dashboard")
@SpringUI(path = "/")
public class DashboardUI extends AbstractSideBarUI {

    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    @Autowired
    ValoSideBar sideBar;

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
    }

    @Override
    protected AbstractSideBar getSideBar() {
        CssLayout headerLayout = new CssLayout();
        headerLayout.addComponent(new MLabel().withIcon(VaadinIcons.USER));
        sideBar.setHeader(headerLayout);
        headerLayout.addStyleName("header-layout");
        return sideBar;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((com.vaadin.demo.dashboard.ui.DashboardUI) getCurrent()).dashboardEventbus;
    }
}
