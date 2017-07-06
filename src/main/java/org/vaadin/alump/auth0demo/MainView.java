package org.vaadin.alump.auth0demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringView(name = "main")
@SpringComponent
@Scope(value = "prototype")
public class MainView extends VerticalLayout implements View {

    @PostConstruct
    protected void init() {
        setMargin(true);
        setSpacing(true);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth(100, Unit.PERCENTAGE);

        Button login = new Button("Login", this::login);
        login.setVisible(!Auth0Session.getCurrent().isLoggedIn());
        Button logout = new Button("Logout", this::logout);
        logout.setVisible(Auth0Session.getCurrent().isLoggedIn());
        buttons.addComponents(login, logout);

        Auth0Session session = Auth0Session.getCurrent();

        Label hello = new Label();
        if(!Auth0Session.getCurrent().isLoggedIn()) {
            hello.setValue("Please login");
        }
        hello.addStyleName(ValoTheme.LABEL_H1);

        addComponents(buttons, hello);

        Auth0Session.getCurrent().getUser().ifPresent(u -> {

            String name = u.getGivenName().orElse(u.getName());
            hello.setValue("Hey, " + name + " logged in :)");

            Label userId = new Label(u.getUserId());
            userId.setCaption("user-id");
            addComponent(userId);

            u.getPicture().ifPresent(p -> {
                Image image = new Image(null, new ExternalResource(p));
                image.addStyleName("gravatar");
                addComponent(image);
            });
        });

    }

    private void login(Button.ClickEvent event) {
        Auth0Session.getCurrent().login();
    }

    private void logout(Button.ClickEvent event) {
        Auth0Session.getCurrent().logout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
