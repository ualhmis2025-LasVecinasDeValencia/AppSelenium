package com.example.application.views.registro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@AnonymousAllowed
@Route(value = "registro")
public class RegistroView extends VerticalLayout {

    public static final ConcurrentMap<String, String> USER_STORE = new ConcurrentHashMap<>();

    public RegistroView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 titulo = new H1("Registro de Usuario");
        TextField usernameField = new TextField("Nombre de Usuario");
        PasswordField passwordField = new PasswordField("Contraseña");

        Button registerButton = new Button("Registrar", event -> {
            String username = usernameField.getValue().trim();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Por favor, completa todos los campos.");
                return;
            }

            if (USER_STORE.containsKey(username)) {
                Notification.show("El nombre de usuario ya existe, elige otro.");
                return;
            }

            USER_STORE.put(username, password);
            Notification.show("Usuario registrado con éxito.");

            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(titulo, usernameField, passwordField, registerButton);
    }
}
