package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.zurnov.bitcoin.insights.domain.Address;
import com.zurnov.bitcoin.insights.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

@Route("address-info")
public class AddressInfoView extends VerticalLayout {

    private final AddressService addressService;

    private final TextField addressField = new TextField("Enter Address");
    private final Button getInfoButton = new Button("Get Address Info");

    public AddressInfoView(@Autowired AddressService addressService) {

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class),
                new RouterLink("Roles", RoleView.class),
                new RouterLink("About Database", AboutDatabaseView.class),
                new RouterLink("Permissions", PermissionView.class),
                new RouterLink("Address Info", AddressInfoView.class)
        );

        add(navBar);

        this.addressService = addressService;

        getInfoButton.addClickListener(event -> getAddressInfo());

        add(addressField, getInfoButton);
    }

    private void getAddressInfo() {
        String address = addressField.getValue();
        if (address.isEmpty()) {
            Notification.show("Please enter an address");
            return;
        }

        Address addressInfo = addressService.getAddressInfo(address).block();
        if (addressInfo != null) {
            showAddressInfoPopup(addressInfo);
        } else {
            Notification.show("Address not found");
        }
    }

    private void showAddressInfoPopup(Address addressInfo) {
        Dialog dialog = new Dialog();
        dialog.setWidth("auto");

        VerticalLayout layout = new VerticalLayout();
        layout.add(
                createTextField("Balance", String.valueOf(addressInfo.getBalance())),
                createTextField("Total BTC", String.valueOf(addressInfo.getBalance().divide(BigInteger.valueOf(100000000)))),
                createTextField("Total Received", String.valueOf(addressInfo.getTotalReceived())),
                createTextField("Total Sent", String.valueOf(addressInfo.getTotalSent())),
                createTextField("Transaction Count", String.valueOf(addressInfo.getNumberOfTransactions()))
        );

        Button closeButton = new Button("Close", event -> dialog.close());

        layout.add(closeButton);
        dialog.add(layout);
        dialog.open();
    }

    private TextField createTextField(String label, String value) {
        TextField textField = new TextField(label);
        textField.setValue(value);
        textField.setWidth("auto");
        textField.setReadOnly(true);
        return textField;
    }

}
