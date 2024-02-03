package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.zurnov.bitcoin.insights.dto.RoleDTO;
import com.zurnov.bitcoin.insights.dto.UserRoleDTO;
import com.zurnov.bitcoin.insights.service.RoleService;
import com.zurnov.bitcoin.insights.service.UserRoleService;

import java.util.List;

@Route("roles")
public class RoleView extends VerticalLayout {

    private final RoleService roleService;
    private final UserRoleService userRoleService;

    private Grid<RoleDTO> roleGrid = new Grid<>(RoleDTO.class);
    private Grid<UserRoleDTO> userRoleGrid = new Grid<>(UserRoleDTO.class);

    public RoleView(RoleService roleService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class),
                new RouterLink("Roles", RoleView.class),
                new RouterLink("About Database", AboutDatabaseView.class)
        );

        Button createRoleButton = new Button("Create Role", event -> openCreateRoleDialog());
        createRoleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteRoleButton = new Button("Delete Role", event -> openDeleteRoleDialog());
        deleteRoleButton.addClassName("delete-button");
        deleteRoleButton.getElement().getStyle().set("background-color", "#d9534f");
        deleteRoleButton.getElement().getStyle().set("color", "#fff");

        Button createUserRoleButton = new Button("Create User Role", event -> openCreateUserRoleDialog());
        createUserRoleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button deleteUserRoleButton = new Button("Delete User Role", event -> openDeleteUserRoleDialog());
        deleteUserRoleButton.addClassName("delete-button");
        deleteUserRoleButton.getElement().getStyle().set("background-color", "#d9534f");
        deleteUserRoleButton.getElement().getStyle().set("color", "#fff");

        VerticalLayout roleLayout = new VerticalLayout();
        roleLayout.add(new Text("Roles"), roleGrid, createRoleButton, deleteRoleButton);

        VerticalLayout userRoleLayout = new VerticalLayout();
        userRoleLayout.add(new Text("User Roles"), userRoleGrid, createUserRoleButton, deleteUserRoleButton);

        add(navBar, roleLayout, userRoleLayout);

        refreshRoles();
        refreshUserRoles();
    }

    private void refreshRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        roleGrid.setItems(roles);
    }

    private void refreshUserRoles() {
        List<UserRoleDTO> userRoles = userRoleService.getAllUserRoles();
        userRoleGrid.setItems(userRoles);
    }

    private void openCreateRoleDialog() {
        Dialog createRoleDialog = new Dialog();

        TextField roleNameField = new TextField("Role Name");
        TextField descriptionField = new TextField("Description");

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.add(roleNameField, descriptionField);

        Notification notification = new Notification();
        notification.setPosition(Notification.Position.MIDDLE);
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        notificationLayout.add(notification);

        Button createRoleButton = new Button("Create Role", event -> {

            String roleNameValue = roleNameField.getValue();
            String roleDescription = descriptionField.getValue();
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleName(roleNameValue);
            roleDTO.setDescription(roleDescription);

            if (!roleService.validateRole(roleDTO)) {
                notification.setText("Role already exists");
                notification.open();
            } else {
                RoleDTO newRole = new RoleDTO();
                newRole.setRoleName(roleNameField.getValue());
                newRole.setDescription(descriptionField.getValue());

                RoleDTO createdRole = roleService.createRole(newRole);

                if (createdRole != null) {
                    notification.setText("Role created successfully");
                    notification.open();
                    refreshRoles();
                    createRoleDialog.close();
                } else {
                    notification.setText("Failed to create role");
                    notification.open();
                }
            }

        });

        Button cancelButton = new Button("Cancel", event -> createRoleDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(createRoleButton, cancelButton);
        buttonsLayout.setSpacing(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(dialogContent, buttonsLayout, notificationLayout);

        createRoleDialog.add(dialogLayout);

        createRoleDialog.open();
    }

    private void openDeleteRoleDialog() {
        Dialog deleteRoleDialog = new Dialog();
        TextField roleIdField = new TextField("Role ID");
        Button deleteRoleConfirmButton = new Button("Delete", event -> {
            Long roleId = Long.valueOf(roleIdField.getValue());
            RoleDTO roleToDelete = roleService.getRoleById(roleId);
            if (roleToDelete != null) {
                showRoleConfirmationDialog(roleToDelete);
            } else {
                Notification.show("Role not found with ID: " + roleId);
            }
            deleteRoleDialog.close();
        });
        deleteRoleDialog.add(new FormLayout(roleIdField, deleteRoleConfirmButton));
        deleteRoleDialog.open();
    }

    private void showRoleConfirmationDialog(RoleDTO roleToDelete) {
        Dialog confirmationDialog = new Dialog();

        VerticalLayout confirmationLayout = new VerticalLayout();

        Text confirmationText = new Text("Are you sure you want to delete this role?");
        confirmationLayout.add(confirmationText);

        VerticalLayout roleInfoLayout = new VerticalLayout(
                new Text("Role Name: " + roleToDelete.getRoleName()),
                new Text("Description: " + roleToDelete.getDescription())
        );

        confirmationLayout.add(roleInfoLayout);

        Button deleteButton = new Button("Delete", event -> {
            roleService.deleteRole(roleToDelete.getRoleId());
            refreshRoles();
            confirmationDialog.close();
        });

        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(deleteButton, cancelButton);
        buttonsLayout.setSpacing(true);

        confirmationLayout.add(buttonsLayout);

        confirmationDialog.add(confirmationLayout);
        confirmationDialog.open();
    }

    private void openCreateUserRoleDialog() {
        Dialog createUserRoleDialog = new Dialog();

        TextField userIdField = new TextField("User ID");
        TextField roleIdField = new TextField("Role ID");

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.add(userIdField, roleIdField);

        Notification notification = new Notification();
        notification.setPosition(Notification.Position.MIDDLE);
        VerticalLayout notificationLayout = new VerticalLayout();
        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        notificationLayout.add(notification);

        Button createUserRoleButton = new Button("Create User Role", event -> {

            Long userIdValue = Long.valueOf(userIdField.getValue());
            Long roleIdValue = Long.valueOf(roleIdField.getValue());
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setRoleId(roleIdValue);
            userRoleDTO.setUserId(userIdValue);
            if (userRoleService.validateUserRole(userRoleDTO)) {
                notification.setText("User Role already exists");
                notification.open();
            } else {
                UserRoleDTO newUserRole = new UserRoleDTO();
                newUserRole.setUserId(userIdValue);
                newUserRole.setRoleId(roleIdValue);

                UserRoleDTO createdUserRole = userRoleService.createUserRole(newUserRole);

                if (createdUserRole != null) {
                    notification.setText("User Role created successfully");
                    notification.open();
                    refreshUserRoles();
                    createUserRoleDialog.close();
                } else {
                    notification.setText("Failed to create user role");
                    notification.open();
                }
            }

        });

        Button cancelButton = new Button("Cancel", event -> createUserRoleDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(createUserRoleButton, cancelButton);
        buttonsLayout.setSpacing(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(dialogContent, buttonsLayout, notificationLayout);

        createUserRoleDialog.add(dialogLayout);

        createUserRoleDialog.open();
    }

    private void openDeleteUserRoleDialog() {
        Dialog deleteUserRoleDialog = new Dialog();
        TextField userRoleIdField = new TextField("User Role ID");
        Button deleteUserRoleConfirmButton = new Button("Delete", event -> {
            Long userRoleId = Long.valueOf(userRoleIdField.getValue());
            UserRoleDTO userRoleToDelete = userRoleService.getUserRoleById(userRoleId);
            if (userRoleToDelete != null) {
                showUserRoleConfirmationDialog(userRoleToDelete);
            } else {
                Notification.show("User Role not found with ID: " + userRoleId);
            }
            deleteUserRoleDialog.close();
        });
        deleteUserRoleDialog.add(new FormLayout(userRoleIdField, deleteUserRoleConfirmButton));
        deleteUserRoleDialog.open();
    }

    private void showUserRoleConfirmationDialog(UserRoleDTO userRoleToDelete) {
        Dialog confirmationDialog = new Dialog();

        VerticalLayout confirmationLayout = new VerticalLayout();

        Text confirmationText = new Text("Are you sure you want to delete this user role?");
        confirmationLayout.add(confirmationText);

        VerticalLayout userRoleInfoLayout = new VerticalLayout(
                new Text("User ID: " + userRoleToDelete.getUserId()),
                new Text("Role ID: " + userRoleToDelete.getRoleId())
        );

        confirmationLayout.add(userRoleInfoLayout);

        Button deleteButton = new Button("Delete", event -> {
            userRoleService.deleteUserRole(userRoleToDelete.getUserRoleId());
            refreshUserRoles();
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
