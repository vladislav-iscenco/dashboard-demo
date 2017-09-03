package com.vaadin.demo.dashboard.view.dashboard;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.call.GeteBayOfficialTimeCall;
import com.vaadin.demo.dashboard.component.NotificationsButton;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Calendar;

@SpringView(name = "")
@SideBarItem(sectionId = "",
        caption = "Dashboard",
        order = 1)
@VaadinFontIcon(VaadinIcons.USER)
@ViewScope
public final class DashboardView extends Panel implements View {

    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private VerticalLayout root;

    final ApiContext apiContext;

    @Autowired
    public DashboardView(ApiContext apiContext) {
        this.apiContext = apiContext;

        setSizeFull();
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        DashboardEventBus.register(this);

        createRoot();

        root.addComponent(buildHeader());
        root.addComponent(buildSparklines());

        Component content = buildContent();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
    }

    private void createRoot() {
        root = new VerticalLayout();
        root.addStyleName("dashboard-view");
        root.setSpacing(false);
        root.setSizeFull();
        setContent(root);
        Responsive.makeResponsive(root);

        // All the open sub-windows should be closed whenever the root layout gets clicked.
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

        HorizontalLayout tools = new HorizontalLayout(notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);
        return header;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        try {
            //Create call object and execute the call
            GeteBayOfficialTimeCall apiCall = new GeteBayOfficialTimeCall(apiContext);
            Calendar cal = apiCall.geteBayOfficialTime();
            dashboardPanels.addComponent(
                    createContentWrapper(new Label("Official eBay Time : " + cal.getTime().toString()))
            );
        } catch (Exception e) {
            System.out.println("Fail to get eBay official time.");
            e.printStackTrace();
        }

        return dashboardPanels;
    }

    private Component createContentWrapper(final Component content) {
        final MCssLayout slot = new MCssLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .withStyleName("dashboard-panel-slot");

        MCssLayout card = new MCssLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .withStyleName(ValoTheme.LAYOUT_CARD);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        MenuItem maximize = tools.addItem("", VaadinIcons.EXPAND,
                selectedItem -> {
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
                selectedItem -> Notification.show("Not implemented in this demo"));
        settings.addSeparator();
        settings.addItem("Close",
                selectedItem -> Notification.show("Not implemented in this demo"));

        // move content caption to wrapper caption
        MLabel caption = new MLabel(content.getCaption())
                .withStyleName(ValoTheme.LABEL_H4)
                .withStyleName(ValoTheme.LABEL_COLORED)
                .withStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MHorizontalLayout toolbar = new MHorizontalLayout()
                .withStyleName("dashboard-panel-toolbar")
                .withWidth(100, Unit.PERCENTAGE)
                .withSpacing(false)
                .with(caption, tools)
                .withComponentAlignment(caption, Alignment.MIDDLE_LEFT)
                .withExpand(caption, 1);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    @Override
    public void enter(final ViewChangeEvent event) {

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
