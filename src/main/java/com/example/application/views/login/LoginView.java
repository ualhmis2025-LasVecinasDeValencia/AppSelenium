package com.example.application.views.login;

import com.example.application.security.AuthenticatedUser;
import com.example.application.views.registro.RegistroView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        // Quitamos el action para usar listener y no form submit HTML
        setAction(null);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("My App");
        i18n.getHeader().setDescription("Login usando usuario registrado");
        
        Anchor registerLink = new Anchor("registro", "¿No tienes cuenta? Regístrate aquí");
        registerLink.getStyle().set("text-decoration", "underline");
        registerLink.getStyle().set("text-align", "center");
        registerLink.getStyle().set("display", "block");
        getFooter().add(registerLink);

        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);

        // Listener para manejar login con el map estático de usuarios
        addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            if (!RegistroView.USER_STORE.containsKey(username) ||
                !RegistroView.USER_STORE.get(username).equals(password)) {
                setError(true);
                return;
            }

            setError(false);
            setOpened(false);
            getUI().ifPresent(ui -> ui.getPage().setLocation("/usuarios"));	
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // No hacemos nada especial
    }
}
