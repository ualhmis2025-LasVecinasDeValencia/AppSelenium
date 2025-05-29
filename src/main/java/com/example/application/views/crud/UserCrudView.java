package com.example.application.views.crud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Collection;
import java.util.TreeMap;



@Route("usuarios")
@PageTitle("Gestión de Usuarios")
@PermitAll


public class UserCrudView extends VerticalLayout {

    private static final TreeMap<Long, Usuario> userStore = new TreeMap<>();
    private static long currentId = 1;

    private final Grid<Usuario> grid = new Grid<>(Usuario.class, false);
    private final TextField username = new TextField("Nombre de usuario");
    private final EmailField email = new EmailField("Email");
    private final TextField password = new TextField("Contraseña");

    private final Button save = new Button("Guardar");
    private final Button delete = new Button("Eliminar");
    private final Button newUser = new Button("Nuevo");

    private Usuario usuarioSeleccionado;
    private final Binder<Usuario> binder = new Binder<>(Usuario.class);

    public UserCrudView() {
        // Configurar grid
        grid.addColumn(Usuario::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Usuario::getUsername).setHeader("Usuario").setAutoWidth(true);
        grid.addColumn(Usuario::getEmail).setHeader("Email").setAutoWidth(true);
        grid.setItems(getAllUsers());

        grid.asSingleSelect().addValueChangeListener(e -> {
            usuarioSeleccionado = e.getValue();
            if (usuarioSeleccionado != null) {
                binder.readBean(usuarioSeleccionado);
            } else {
                clearForm();
            }
        });

        // Formulario
        FormLayout form = new FormLayout(username, email, password);
        binder.bindInstanceFields(this);

        // Botones
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        newUser.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        save.addClickListener(e -> guardarUsuario());
        delete.addClickListener(e -> eliminarUsuario());
        newUser.addClickListener(e -> nuevoUsuario());

        HorizontalLayout botones = new HorizontalLayout(save, delete, newUser);

        add(grid, form, botones);
    }

    private Collection<Usuario> getAllUsers() {
        return userStore.values();
    }

    private void guardarUsuario() {
        if (usuarioSeleccionado == null || usuarioSeleccionado.getId() == null) {
            usuarioSeleccionado = new Usuario();
            usuarioSeleccionado.setId(currentId++);
        }
        try {
            binder.writeBean(usuarioSeleccionado);
            userStore.put(usuarioSeleccionado.getId(), usuarioSeleccionado);
            Notification.show("Usuario guardado");
            actualizarGrid();
            clearForm();
        } catch (ValidationException e) {
            Notification.show("Error al guardar");
        }
    }

    private void eliminarUsuario() {
        if (usuarioSeleccionado != null) {
            userStore.remove(usuarioSeleccionado.getId());
            Notification.show("Usuario eliminado");
            actualizarGrid();
            clearForm();
        }
    }

    private void nuevoUsuario() {
        usuarioSeleccionado = new Usuario();
        binder.readBean(usuarioSeleccionado);
    }

    private void actualizarGrid() {
        grid.setItems(getAllUsers());
    }

    private void clearForm() {
        usuarioSeleccionado = null;
        binder.readBean(new Usuario());
    }

    // ✅ Clase interna para representar un usuario
    public static class Usuario {
        private Long id;
        private String username;
        private String email;
        private String password;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
