package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.zurnov.bitcoin.insights.dto.UserDTO;
import com.zurnov.bitcoin.insights.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Route("user")
public class UserView extends VerticalLayout {

    private final UserService userService;

    private Grid<UserDTO> userGrid = new Grid<>(UserDTO.class);

    public UserView(UserService userService) {
        this.userService = userService;

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class)
        );

        Button createButton = new Button("Create", event -> openCreateDialog());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteButton = new Button("Delete", event -> openDeleteDialog());
        deleteButton.addClassName("delete-button");
        deleteButton.getElement().getStyle().set("background-color", "#d9534f");
        deleteButton.getElement().getStyle().set("color", "#fff");

        Button refreshButton = new Button("Refresh", event -> refreshUsers());
        refreshButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);


        userGrid.setColumns("userId","username", "email", "dateOfBirth", "registrationDate");
        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.add(createButton, deleteButton, refreshButton);

        add(navBar, userGrid, actionButtons);

        refreshUsers();
    }

    private void refreshUsers() {
        List<UserDTO> users = userService.getAllUsers();
        userGrid.setItems(users);
    }

    private void openCreateDialog() {
        Dialog createDialog = new Dialog();

        TextField usernameField = new TextField("Username");
        TextField emailField = new TextField("Email");
        DatePicker dobField = new DatePicker("Date of Birth");
        PasswordField passwordField = new PasswordField("Password");

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.add(usernameField, emailField, dobField, passwordField);

        Notification notification = new Notification();
        notification.setPosition(Notification.Position.MIDDLE);
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        notificationLayout.add(notification);

        Button createUserButton = new Button("Create User", event -> {

            String usernameValue = usernameField.getValue();
            String emailValue = emailField.getValue();

            if (userService.existsByUsername(usernameValue)) {
                notification.setText("Username already exists");
                notification.open();
            } else if (userService.existsByEmail(emailValue)) {
                notification.setText("Email already exists");
                notification.open();
            } else {
                UserDTO newUser = new UserDTO();
                newUser.setUsername(usernameField.getValue());
                newUser.setEmail(emailField.getValue());
                newUser.setPasswordHash(passwordField.getValue());
                newUser.setDateOfBirth(LocalDateTime.of(
                        dobField.getValue().getYear(),
                        dobField.getValue().getMonth(),
                        dobField.getValue().getDayOfMonth(),
                        0,
                        0));
                newUser.setRegistrationDate(LocalDateTime.now());

                UserDTO createdUser = userService.createUser(newUser);

                if (createdUser != null) {
                    notification.setText("User created successfully");
                    notification.open();
                    refreshUsers();
                    createDialog.close();
                } else {
                    notification.setText("Failed to create user");
                    notification.open();
                }
            }

        });

        Button cancelButton = new Button("Cancel", event -> createDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(createUserButton, cancelButton);
        buttonsLayout.setSpacing(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(dialogContent, buttonsLayout, notificationLayout);

        createDialog.add(dialogLayout);

        createDialog.open();
    }


    private void openDeleteDialog() {
        Dialog deleteDialog = new Dialog();
        TextField userIdField = new TextField("User ID");
        Button deleteConfirmButton = new Button("Delete", event -> {
            Long userId = Long.valueOf(userIdField.getValue());
            UserDTO userToDelete = userService.getUserById(userId);
            if (userToDelete != null) {
                showConfirmationDialog(userToDelete);
            } else {
                Notification.show("User not found with ID: " + userId);
            }
            deleteDialog.close();
        });
        deleteDialog.add(new FormLayout(userIdField, deleteConfirmButton));
        deleteDialog.open();
    }

    private void showConfirmationDialog(UserDTO userToDelete) {
        Dialog confirmationDialog = new Dialog();

        VerticalLayout confirmationLayout = new VerticalLayout();

        Text confirmationText = new Text("Are you sure you want to delete this user?");
        confirmationLayout.add(confirmationText);

        VerticalLayout userInfoLayout = new VerticalLayout(
                new Text("Username: " + userToDelete.getUsername()),
                new Text("Email: " + userToDelete.getEmail())

        );

        confirmationLayout.add(userInfoLayout);

        Button deleteButton = new Button("Delete", event -> {
            userService.deleteUser(userToDelete.getUserId());
            refreshUsers();
            confirmationDialog.close();
        });

        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(deleteButton, cancelButton);
        buttonsLayout.setSpacing(true);

        confirmationLayout.add(buttonsLayout);

        confirmationDialog.add(confirmationLayout);
        confirmationDialog.open();
    }

}
