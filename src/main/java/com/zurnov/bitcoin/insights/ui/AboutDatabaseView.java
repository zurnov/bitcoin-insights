package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

@Route("about/db")
public class AboutDatabaseView extends VerticalLayout {


    public AboutDatabaseView() {

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class),
                new RouterLink("Roles", RoleView.class),
                new RouterLink("About Database", AboutDatabaseView.class)
        );
        add(navBar);
        StreamResource imageResource = new StreamResource("db_scheme.png",
                () -> getClass().getResourceAsStream("/static/images/db_scheme.png"));


        Image image = new Image(imageResource, "image");

        Button redirectButton = new Button("Go to GitHub Repository", event ->
                getUI().ifPresent(ui -> ui.getPage().executeJs("window.open('https://github.com/zurnov/bitcoin-insights','_blank');")));

        add(image);
        add(redirectButton);

    }
}
