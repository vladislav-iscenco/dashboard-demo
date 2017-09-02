package com.vaadin.demo.dashboard.view;

import com.vaadin.demo.dashboard.view.dashboard.DashboardView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import java.util.Arrays;

public enum DashboardViewType {
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
                              final Class<? extends View> viewClass, final Resource icon,
                              final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        return Arrays.stream(values())
                .filter(view -> view.getViewName().equals(viewName))
                .findFirst().orElse(null);
    }

}
