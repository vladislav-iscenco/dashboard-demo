package com.vaadin.demo.dashboard.view.dashboard;

import com.vaadin.demo.dashboard.component.NotificationsButton;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.dashboard.DashboardEdit.DashboardEditListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View,
        DashboardEditListener {

    public static final String EDIT_ID = "dashboard-edit";
    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private final VerticalLayout root;

    public DashboardView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        root.addComponent(buildSparklines());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(event ->
                DashboardEventBus.post(new CloseOpenWindowsEvent()));
    }

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);

        return sparks;
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel = new Label("Dashboard");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        notificationsButton = new NotificationsButton();
        Component edit = buildEditButton();
        HorizontalLayout tools = new HorizontalLayout(notificationsButton, edit);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Component buildEditButton() {
        Button result = new Button();
        result.setId(EDIT_ID);
        result.setIcon(VaadinIcons.EDIT);
        result.addStyleName("icon-edit");
        result.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        result.setDescription("Edit Dashboard");
        result.addClickListener(event -> getUI().addWindow(
                new DashboardEdit(DashboardView.this, titleLabel.getValue())));
        return result;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(new Label("TEST"));

        return dashboardPanels;
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");
        toolbar.setSpacing(false);

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        MenuItem maximize = tools.addItem("", VaadinIcons.EXPAND,
                (Command) selectedItem -> {
                    if (!slot.getStyleName().contains("maximize")) {
                        selectedItem.setIcon(VaadinIcons.COMPRESS);
                        toggleMaximized(slot, true);
                    } else {
                        slot.removeStyleName("maximize");
                        selectedItem.setIcon(VaadinIcons.EXPAND);
                        toggleMaximized(slot, false);
                    }
                });
        maximize.setStyleName("icon-only");

        MenuItem settings = tools.addItem("", VaadinIcons.COG, null);
        settings.addItem("Configure",
                (Command) selectedItem ->
                        Notification.show("Not implemented in this demo"));
        settings.addSeparator();
        settings.addItem("Close",
                (Command) selectedItem ->
                        Notification.show("Not implemented in this demo"));

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
        notificationsButton.updateNotificationsCount(null);
    }

    @Override
    public void dashboardNameEdited(final String name) {
        titleLabel.setValue(name);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Component aRoot : root) {
            aRoot.setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Component c : dashboardPanels) {
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

}
