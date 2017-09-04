package com.vaadin.demo.dashboard.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.SideBarItemDescriptor;
import org.vaadin.spring.sidebar.SideBarUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;

import javax.annotation.PostConstruct;

/**
 * Created by Ronen on 9/5/2017.
 */

@Component
public class DashboardSideBar extends ValoSideBar {

    private MenuBar.MenuItem settingsItem;

    public DashboardSideBar(SideBarUtils sideBarUtils) {
        super(sideBarUtils);
    }

    @Override
    protected CssLayout createCompositionRoot() {
        CssLayout sidebarContent = super.createCompositionRoot();
        sidebarContent.setWidth(null);
        sidebarContent.setHeight("100%");
        sidebarContent.addStyleName("sidebar");
        sidebarContent.addStyleName("no-vertical-drag-hints");
        sidebarContent.addStyleName("no-horizontal-drag-hints");
        sidebarContent.addStyleName(ValoTheme.MENU_PART);
        if (isLargeIcons()) {
            sidebarContent.addStyleName(ValoTheme.MENU_PART_LARGE_ICONS);
        }

        sidebarContent.addComponent(buildHeader());
        sidebarContent.addComponent(buildUserMenu());
        return sidebarContent;
    }

    private com.vaadin.ui.Component buildHeader() {
        Label logo = new Label(
                "QuickTickets <strong>Dashboard</strong>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        logoWrapper.setSpacing(false);
        return logoWrapper;
    }

    private com.vaadin.ui.Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        settingsItem = settings.addItem("",
                new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(new DashboardEvent.ProfileUpdatedEvent("Vladislav Iscenco"));
//        settingsItem.addItem("Edit Profile", (MenuBar.Command) selectedItem ->
//                ProfilePreferencesWindow.open(user, false));
//        settingsItem.addItem("Preferences", (MenuBar.Command) selectedItem ->
//                ProfilePreferencesWindow.open(user, true));
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", (MenuBar.Command) selectedItem ->
                DashboardEventBus.post(new DashboardEvent.UserLoggedOutEvent()));
        return settings;
    }

    @Override
    protected ItemComponentFactory createDefaultItemComponentFactory() {
        return new DefaultItemComponentFactory() {
            @Override
            public com.vaadin.ui.Component createItemComponent(SideBarItemDescriptor descriptor) {
                com.vaadin.ui.Component menuItem = super.createItemComponent(descriptor);
                menuItem.addStyleName("valo-menuitems");
                return menuItem;
            }
        };
    }

    @Subscribe
    public void updateUserName(final DashboardEvent.ProfileUpdatedEvent event) {
        settingsItem.setText(event.getName());
    }
}
