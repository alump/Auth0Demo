package org.vaadin.alump.auth0demo;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
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

    private ProgressBar spinner;
    private Label errorLabel;
    private Label errorDescLabel;

    private AuthenticationController authenticationController;

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

    private void checkAuthentication(VaadinRequest request) {
        VaadinServletRequest servletRequest = (VaadinServletRequest) request;

        try {

            Tokens tokens = getAuthenticationController().handle(servletRequest.getHttpServletRequest());
            UserInfo userInfo = Auth0Util.resolveUser(tokens.getAccessToken());

            Auth0Session.getCurrent().setAuth0Info(tokens, userInfo);
            Page.getCurrent().open("/", null);

        } catch(IdentityVerificationException e) {
            if("a0.missing_authorization_code".equals(e.getCode())) {

                String url = getAuthenticationController().buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();
                Page.getCurrent().open(url, null);

            } else {
                showError(e);
            }
        } catch(Exception e) {
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

    @Override
    protected void init(VaadinRequest request) {
        checkAuthentication(request);
    }


    protected AuthenticationController getAuthenticationController() {
        if(authenticationController == null) {
            Properties properties = Auth0Util.getAuth0Properties();
            authenticationController = AuthenticationController.newBuilder(
                    properties.getProperty("auth0.domain"),
                    properties.getProperty("auth0.clientId"),
                    properties.getProperty("auth0.clientSecret")).build();
        }

        return authenticationController;
    }

}
