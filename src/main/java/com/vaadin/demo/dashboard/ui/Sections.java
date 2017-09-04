package com.vaadin.demo.dashboard.ui;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * Created by Ronen on 9/4/2017.
 */
@SideBarSections({
        @SideBarSection(id = Sections.MAIN, caption = ""),
        @SideBarSection(id = Sections.SETTINGS, caption = "Settings"),
})
@Component
public class Sections {
    public static final String MAIN = "main";
    public static final String SETTINGS = "settings";
}
