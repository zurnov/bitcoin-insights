package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;

@Route(value = "")
public class HomeView extends VerticalLayout {

    public HomeView() {

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class),
                new RouterLink("Roles", RoleView.class),
                new RouterLink("About Database", AboutDatabaseView.class),
                new RouterLink("Permissions", PermissionView.class)
        );

        add(navBar);
        add(new Text("Welcome to the Home View!"));
        add(new Text("This is a simple home page."));

        StreamResource imageResource = new StreamResource("logo.jpg",
                () -> getClass().getResourceAsStream("/static/images/logo.jpg"));


        Image image = new Image(imageResource, "logo");
        add(image);
    }
}
