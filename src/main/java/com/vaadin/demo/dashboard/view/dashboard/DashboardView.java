package com.vaadin.demo.dashboard.view.dashboard;

import com.ebay.sdk.ApiContext;
import com.vaadin.demo.dashboard.ui.AbstractSideBarUI;
import com.vaadin.demo.dashboard.ui.Sections;
import com.vaadin.demo.dashboard.view.AbstractView;
import com.vaadin.icons.VaadinIcons;
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

import java.time.LocalDateTime;

import static com.vaadin.demo.dashboard.view.dashboard.DashboardView.VIEW_NAME;
import static com.vaadin.demo.dashboard.view.dashboard.DashboardView.VIEW_PATH;

@SpringView(name = VIEW_PATH)
@SideBarItem(sectionId = Sections.MAIN,
        caption = VIEW_NAME,
        order = 1)
@VaadinFontIcon(VaadinIcons.SPECIALIST)
@ViewScope
public final class DashboardView extends AbstractView {

    public static final String VIEW_PATH = "";
    public static final String VIEW_NAME = "Dashboard";
    public static final String VIEW_TITLE = "Dashboard";
    public static final VaadinIcons VIEW_ICON = VaadinIcons.SPECIALIST;

    private CssLayout dashboardPanels;

    private final ApiContext apiContext;

    @Autowired
    public DashboardView(ApiContext apiContext) {
        super();
        this.apiContext = apiContext;
        setViewPath(VIEW_PATH);
        setViewName(VIEW_NAME);
        setViewTitle(VIEW_TITLE);
        setViewIcon(VIEW_ICON);

        // workaround to save styles
        ((AbstractSideBarUI) UI.getCurrent())
                .getRightContainer().addStyleName("dashboard-view");
    }

    protected Component createContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        try {
//            GeteBayOfficialTimeCall apiCall = new GeteBayOfficialTimeCall(apiContext);
//            Date time = apiCall.geteBayOfficialTime().getTime();
            Label label = new Label("Official eBay Time : "
//                    + time.toString());
                    + LocalDateTime.now().toString());

            dashboardPanels.addComponent(wrapContentPart(label));

        } catch (Exception e) {
            System.out.println("Fail to get eBay official time.");
            e.printStackTrace();
        }
        return dashboardPanels;
    }

    private Component wrapContentPart(final Component content) {
        final MCssLayout slot = new MCssLayout()
                .withWidth(100, Unit.PERCENTAGE)
                .withStyleName("dashboard-panel-slot");

        MCssLayout card = new MCssLayout()
                .withFullSize()
                .withStyleName(ValoTheme.LAYOUT_CARD);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);

        MenuItem maximize = tools.addItem("", VaadinIcons.EXPAND,
                selectedItem -> {
                    if (!slot.getStyleName().contains("max")) {
                        selectedItem.setIcon(VaadinIcons.COMPRESS);
                        toggleMaximized(slot, true);
                    } else {
                        slot.removeStyleName("max");
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

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Component aRoot : contentWrapper) {
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
