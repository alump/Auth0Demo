package org.vaadin.alump.auth0demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringView(name = "main")
@SpringComponent
@Scope(value = "prototype")
public class MainView extends VerticalLayout implements View {

    @Autowired
    private Auth0Management management;

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
            hello.setValue("Hey " + u.getGivenName().orElse(u.getName()) + "!");

            HorizontalLayout info = new HorizontalLayout();
            info.setSpacing(true);
            info.setWidth(100, Unit.PERCENTAGE);
            addComponent(info);

            Component userinfo = createUserInfoGrid(u);
            info.addComponent(userinfo);
            userinfo.setWidth(100, Unit.PERCENTAGE);
            info.setExpandRatio(userinfo, 1f);

            u.getPicture().ifPresent(url -> {
                Image image = new Image(null, new ExternalResource(url));
                image.setWidth(200, Unit.PIXELS);
                image.setHeight(200, Unit.PIXELS);
                info.addComponents(image);
            });
        });

        Label mgnLabel = new Label();
        mgnLabel.setWidth(100, Unit.PERCENTAGE);
        addComponent(mgnLabel);
        try {
            if(management.isEnabled()) {
                mgnLabel.setValue("Found " + management.getUsers().size() + " users from Auth0");
            } else {
                mgnLabel.setValue("Management API key not defined");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mgnLabel.setValue("Failed to access management API");
            mgnLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        }

    }

    private Component createUserInfoGrid(Auth0User user) {
        FormLayout layout = new FormLayout();

        user.getKeys().stream().filter(key -> !key.equals("clientID")).forEach(key -> {
            try {
                String value = user.getValue(key);

                Label row = new Label(value);
                row.setCaption(key);
                layout.addComponent(row);
            } catch(Exception e) {
                System.err.println("Failed to read property " + key);
                e.printStackTrace();
            }
        });

        return layout;
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
