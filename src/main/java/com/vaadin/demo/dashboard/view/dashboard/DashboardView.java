package com.vaadin.demo.dashboard.view.dashboard;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.GeteBayOfficialTimeCall;
import com.vaadin.demo.dashboard.component.NotificationsButton;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.IOException;
import java.util.Calendar;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View {

    public static final String TITLE_ID = "dashboard-title";

    private Label titleLabel;
    private NotificationsButton notificationsButton;
    private CssLayout dashboardPanels;
    private VerticalLayout root;

    public DashboardView() {
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

        // TODO: 9/3/2017 content
        try {
            // Instantiate  ApiContext and initialize with token and Trading API URL
            ApiContext apiContext = getApiContext();

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
        notificationsButton.updateNotificationsCount(null);
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

    private static ApiContext getApiContext() throws IOException {
        ApiContext apiContext = new ApiContext();
        ApiCredential cred = apiContext.getApiCredential();
        cred.seteBayToken(token);
        apiContext.setApiServerUrl(sandboxGatewayURL);
        return apiContext;
    }

    private final static String token = "AgAAAA**AQAAAA**aAAAAA**JyyrWQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GkAZKLpgydj6x9nY+seQ**Kk8EAA**AAMAAA**4Wrp9Ct1PcG5zUFU9NXunyYPjo2gUHKMmKrDMGOxcVbMmx1TusgSUxLe4FTM6PfiMbAa7iIpesEHWgreQTL3K0djS4BEsOP2HwdE8SAmgaNrgc+e/kZ2JmDjsX0+A3edBpdi1EPfiQvTmwHFiup+UNVIfIB6MQ2pBTk6VrMaFQHmqBgdivrzxaJYGL2OyCgSDsQAIIKDOjBxCiqKdfIUU9M5mNteBePDPoG4WFtnTJKZ1a2crs2DY3bq4oQwNHSVBBzTex8HtBxpcf+mzUT48N0tN2VDXReZ+liENYhWV2yGf66Dsca72/MsyNRzSprogiMEbGBpmuB95wghbUr2IGiKgAY8Htg4uQvQQX3L+CvOiCAzLhmjQnUre+OlIW1sr/RwWJp3EUL6ZJ8pbNP0G0QNpj4eE42PdyoxDbuMCpq1+ElJapj4vg79gI0TWPNKOC8ZURQm4SS9w29H+z1zwbqmDGDNXzI+JUGTBqFKitLZS2oYjvbtFaw7FBvpPgMO6giWKpOsm+pYV0v1k7RqvMltZYhpUMbSx2vXUVnJ8YV5th/SCxjsXQhMv6eUhWLVpsGL7eGoo1DWRNyVEbjGS/u/Yp4QpIWlRHSVSK+TZd+JDPmR8i7bybzVFw4J7+Eo2og1y/a2rhRya5wqDlGZKlOZjY//JBLZzCn52grOIGKLLnSQP381ZICqkPnf7MARyM7CIStTZFwlShBVlm23ydN4i++eSw+E1n9bwKS5fDmMxQWfNKO01/6El+RsjbO2";

    private final static String sandboxGatewayURL = "https://api.sandbox.ebay.com/wsapi";

    private final static String productionGatewayURL = "https://api.ebay.com/wsapi";

}
