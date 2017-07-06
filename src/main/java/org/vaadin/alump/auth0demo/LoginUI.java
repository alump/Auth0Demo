package org.vaadin.alump.auth0demo;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by alump on 05/07/2017.
 */
@SpringUI(path = "/login")
@Theme("valo")
public class LoginUI extends UI {

    private AuthenticationController authenticationController;

    private ProgressBar spinner;
    private Label errorLabel;
    private Label errorDescLabel;

    private static Properties auth0Properties;

    public LoginUI() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("wait-for-login");
        layout.setMargin(true);
        layout.setSpacing(true);

        spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        spinner.setWidth(200, Unit.PIXELS);
        spinner.setHeight(200, Unit.PIXELS);
        layout.addComponent(spinner);
        layout.setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);

        errorLabel = new Label("Something went wrong :(");
        errorLabel.setWidth(100, Unit.PERCENTAGE);
        errorLabel.addStyleName(ValoTheme.LABEL_H1);
        errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setVisible(false);

        errorDescLabel = new Label("n/a");
        errorDescLabel.setWidth(100, Unit.PERCENTAGE);
        errorDescLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorDescLabel.setVisible(false);

        layout.addComponents(errorLabel, errorDescLabel);

    }

    public static String getLoginURL() {
        return getAuth0Properties().getProperty("auth0.loginUrl");
    }

    private void checkAuthentication(VaadinRequest request) {
        VaadinServletRequest servletRequest = (VaadinServletRequest) request;

        try {

            Tokens tokens = getAuthenticationController().handle(servletRequest.getHttpServletRequest());
            UserInfo userInfo = getAuth().userInfo(tokens.getAccessToken()).execute();

            Auth0Session.getCurrent().setAuth0Info(tokens, userInfo);
            Page.getCurrent().open("/", null);

        } catch(IdentityVerificationException e) {
            if("a0.missing_authorization_code".equals(e.getCode())) {

                String url = getAuthenticationController().buildAuthorizeUrl(servletRequest, getLoginURL()).build();
                Page.getCurrent().open(url, null);

            } else {
                showError(e);
            }
        } catch(Auth0Exception e) {
            showError(e);
        }
    }

    private void showError(Throwable t) {
        spinner.setVisible(false);
        errorLabel.setVisible(true);
        errorDescLabel.setVisible(true);
        errorDescLabel.setValue(t.getMessage());
        t.printStackTrace();
    }

    private static Properties getAuth0Properties() {
        if(auth0Properties == null) {
            Properties properties = new Properties();

            try {
                InputStream stream = Auth0Session.class.getClassLoader().getResourceAsStream(
                        "auth0-default.properties");
                properties.load(stream);
            } catch(IOException e) {
                e.printStackTrace();
            }

            try {
                InputStream stream = Auth0Session.class.getClassLoader().getResourceAsStream("auth0.properties");
                properties.load(stream);
            } catch(IOException e) {
                throw new RuntimeException("Please add missing auth0.properties file", e);
            }

            auth0Properties = properties;
        }

        return auth0Properties;
    }

    private AuthenticationController getAuthenticationController() {
        if(authenticationController == null) {
            Properties properties = getAuth0Properties();
            authenticationController = AuthenticationController.newBuilder(
                    properties.getProperty("auth0.domain"),
                    properties.getProperty("auth0.clientId"),
                    properties.getProperty("auth0.clientSecret")).build();
        }

        return authenticationController;
    }

    private AuthAPI getAuth() {
        Properties properties = getAuth0Properties();
        return new AuthAPI(properties.getProperty("auth0.domain"),
                properties.getProperty("auth0.clientId"),
                properties.getProperty("auth0.clientSecret"));
    }

    @Override
    protected void init(VaadinRequest request) {
        checkAuthentication(request);
    }
}
