package com.zurnov.bitcoin.insights.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.zurnov.bitcoin.insights.dto.PermissionDTO;
import com.zurnov.bitcoin.insights.dto.RoleDTO;
import com.zurnov.bitcoin.insights.dto.RolePermissionDTO;
import com.zurnov.bitcoin.insights.service.PermissionService;
import com.zurnov.bitcoin.insights.service.RolePermissionService;
import com.zurnov.bitcoin.insights.service.RoleService;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Route(value = "permissions-view")
public class PermissionView extends VerticalLayout {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    private final Grid<PermissionDTO> permissionGrid;
    private final Grid<RoleDTO> roleGrid;
    private final Grid<RolePermissionInfo> rolePermissionGrid;
    private final Dialog permissionDialog;
    private final Dialog roleDialog;

    private final Grid<RoleWithPermissions> roleWithPermissionsGrid;

    public PermissionView(PermissionService permissionService,
                          RoleService roleService,
                          RolePermissionService rolePermissionService) {

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.add(
                new RouterLink("Home", HomeView.class),
                new RouterLink("User", UserView.class),
                new RouterLink("Roles", RoleView.class),
                new RouterLink("About Database", AboutDatabaseView.class),
                new RouterLink("Permissions", PermissionView.class)
        );
        add(navBar);

        this.permissionService = permissionService;
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;

        permissionGrid = new Grid<>(PermissionDTO.class);
        roleGrid = new Grid<>(RoleDTO.class);
        rolePermissionGrid = new Grid<>(RolePermissionInfo.class);
        roleWithPermissionsGrid = new Grid<>(RoleWithPermissions.class);

        Button addPermissionButton = new Button("Add Permission");
        Button deletePermissionButton = new Button("Delete Permission");
        Button addRoleButton = new Button("Add Role");
        Button deleteRoleButton = new Button("Delete Role");
        Button assignPermissionButton = new Button("Assign Permission to Role");
        Button removePermissionButton = new Button("Remove Permission from Role");

        permissionDialog = new Dialog();
        roleDialog = new Dialog();

        configurePermissionDialog();
        configureRoleDialog();

        addPermissionButton.addClickListener(event -> permissionDialog.open());
        deletePermissionButton.addClickListener(event -> deletePermission());
        addRoleButton.addClickListener(event -> roleDialog.open());
        deleteRoleButton.addClickListener(event -> deleteRole());
        assignPermissionButton.addClickListener(event -> assignPermission());
        removePermissionButton.addClickListener(event -> removePermission());

        add(roleWithPermissionsGrid, permissionGrid, addPermissionButton, deletePermissionButton,
                roleGrid, addRoleButton, deleteRoleButton,
                rolePermissionGrid, assignPermissionButton, removePermissionButton);

        refreshData();
    }

    private void refreshData() {
        permissionGrid.setItems(permissionService.getAllPermissions());
        roleGrid.setItems(roleService.getAllRoles());
        rolePermissionGrid.setItems(generateRolePermissionInfos());

        displayData();
    }

    private void displayData() {

        roleWithPermissionsGrid.removeAllColumns();

        roleWithPermissionsGrid.addColumn(RoleWithPermissions::getRoleName)
                .setHeader("Role Name");
        roleWithPermissionsGrid.addColumn(RoleWithPermissions::getDescription)
                .setHeader("Role Description");

        roleWithPermissionsGrid.addComponentColumn(roleWithPermissions -> {
            List<PermissionDTO> permissions = roleWithPermissions.getPermissions();

            VerticalLayout permissionLayout = new VerticalLayout();
            permissionLayout.setPadding(false);
            permissionLayout.setSpacing(true);
            for (PermissionDTO permission : permissions) {
                permissionLayout.add(new Span(permission.getPermissionName()));
            }
            return permissionLayout;
        }).setHeader("Permissions");


        List<RoleWithPermissions> rolesWithPermissions = generateRolesWithPermissions();
        roleWithPermissionsGrid.setItems(rolesWithPermissions);
    }

    private List<RoleWithPermissions> generateRolesWithPermissions() {
        List<RoleWithPermissions> rolesWithPermissions = new ArrayList<>();

        List<RoleDTO> roles = roleService.getAllRoles();
        for (RoleDTO role : roles) {
            List<PermissionDTO> permissions = getPermissionsForRole(role);
            rolesWithPermissions.add(new RoleWithPermissions(role, permissions));
        }
        return rolesWithPermissions;
    }

    private List<PermissionDTO> getPermissionsForRole(RoleDTO role) {
        List<PermissionDTO> permissions = new ArrayList<>();
        // Iterate over role permissions and fetch permission details
        List<RolePermissionDTO> rolePermissions = rolePermissionService.getRolePermissionsByRoleId(role.getRoleId());
        for (RolePermissionDTO rolePermission : rolePermissions) {
            PermissionDTO permission = permissionService.getPermissionById(rolePermission.getPermissionId());
            permissions.add(permission);
        }
        return permissions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RolePermissionInfo {
        private String roleName;
        private String permissionDescription;
        private Long rolePermissionId;

        public RolePermissionInfo(Long rolePermissionId, String roleName, String permissionDescription) {
            this.rolePermissionId = rolePermissionId;
            this.roleName = roleName;
            this.permissionDescription = permissionDescription;
        }
    }

    private List<RolePermissionInfo> generateRolePermissionInfos() {
        List<RolePermissionInfo> rolePermissionInfos = new ArrayList<>();
        List<RolePermissionDTO> rolePermissions = rolePermissionService.getAllRolePermissions();
        for (RolePermissionDTO rolePermission : rolePermissions) {
            RoleDTO role = roleService.getRoleById(rolePermission.getRoleId());
            PermissionDTO permission = permissionService.getPermissionById(rolePermission.getPermissionId());
            rolePermissionInfos.add(new RolePermissionInfo(rolePermission.getRolePermissionId(),role.getRoleName(), permission.getDescription()));
        }
        return rolePermissionInfos;
    }

    @Data
    public class RolePermissionViewDTO {

        private Long rolePermissionId;

        private Long roleId;

        private String roleName;

        private Long permissionId;
    }


    @Getter
    public static class RoleWithPermissions {
        private final RoleDTO role;
        private final List<PermissionDTO> permissions;

        public RoleWithPermissions(RoleDTO role, List<PermissionDTO> permissions) {
            this.role = role;
            this.permissions = permissions;
        }

        public String getRoleName() {
            return role.getRoleName();
        }

        public String getDescription() {
            return role.getDescription();
        }
    }

    private void configurePermissionDialog() {
        FormLayout formLayout = new FormLayout();
        TextField permissionNameField = new TextField("Permission Name");
        TextField descriptionField = new TextField("Description");
        Button savePermissionButton = new Button("Save");
        Button cancelPermissionButton = new Button("Cancel");

        savePermissionButton.addClickListener(event -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            permissionDTO.setPermissionName(permissionNameField.getValue());
            permissionDTO.setDescription(descriptionField.getValue());
            permissionService.createPermission(permissionDTO);
            refreshData();
            permissionDialog.close();
        });

        cancelPermissionButton.addClickListener(event -> permissionDialog.close());

        formLayout.add(permissionNameField, descriptionField, savePermissionButton, cancelPermissionButton);
        permissionDialog.add(formLayout);
    }

    private void configureRoleDialog() {
        FormLayout formLayout = new FormLayout();
        TextField roleNameField = new TextField("Role Name");
        TextField descriptionField = new TextField("Description");
        Button saveRoleButton = new Button("Save");
        Button cancelRoleButton = new Button("Cancel");

        saveRoleButton.addClickListener(event -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleName(roleNameField.getValue());
            roleDTO.setDescription(descriptionField.getValue());
            roleService.createRole(roleDTO);
            refreshData();
            roleDialog.close();
        });

        cancelRoleButton.addClickListener(event -> roleDialog.close());

        formLayout.add(roleNameField, descriptionField, saveRoleButton, cancelRoleButton);
        roleDialog.add(formLayout);
    }

    private void deletePermission() {
        PermissionDTO selectedPermission = permissionGrid.asSingleSelect().getValue();
        if (selectedPermission != null) {
            permissionService.deletePermission(selectedPermission.getPermissionId());
            refreshData();
            Notification.show("Permission deleted");
        } else {
            Notification.show("Please select a permission to delete");
        }
    }

    private void deleteRole() {
        RoleDTO selectedRole = roleGrid.asSingleSelect().getValue();
        if (selectedRole != null) {
            roleService.deleteRole(selectedRole.getRoleId());
            refreshData();
            Notification.show("Role deleted");
        } else {
            Notification.show("Please select a role to delete");
        }
    }

    private void assignPermission() {
        RoleDTO selectedRole = roleGrid.asSingleSelect().getValue();
        PermissionDTO selectedPermission = permissionGrid.asSingleSelect().getValue();
        if (selectedRole != null && selectedPermission != null) {
            RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
            rolePermissionDTO.setRoleId(selectedRole.getRoleId());
            rolePermissionDTO.setPermissionId(selectedPermission.getPermissionId());
            rolePermissionService.createRolePermission(rolePermissionDTO);
            refreshData();
            Notification.show("Permission assigned to role");
        } else {
            Notification.show("Please select a role and a permission to assign");
        }
    }

    private void removePermission() {
        RolePermissionInfo selectedRolePermission = rolePermissionGrid.asSingleSelect().getValue();
        if (selectedRolePermission != null) {
            rolePermissionService.deleteRolePermission(selectedRolePermission.getRolePermissionId());
            refreshData();
            Notification.show("Permission removed from role");
        } else {
            Notification.show("Please select a role permission to remove");
        }
    }
}
