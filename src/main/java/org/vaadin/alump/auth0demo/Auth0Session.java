package org.vaadin.alump.auth0demo;

import com.auth0.*;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.UserInfo;
import com.vaadin.server.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;


public class Auth0Session extends VaadinSession {

    private Tokens auth0Tokens;
    private UserInfo auth0UserInfo;
    private static Properties auth0Properties;

    public Auth0Session(VaadinService service) {
        super(service);
    }

    public Optional<Tokens> getAuth0Tokens() {
        return Optional.ofNullable(auth0Tokens);
    }

    public Optional<UserInfo> getAuth0UserInfo() {
        return Optional.ofNullable(auth0UserInfo);
    }

    public void setAuth0Info(Tokens tokens, UserInfo userInfo) {
        this.auth0Tokens = tokens;
        this.auth0UserInfo = userInfo;
    }

    public boolean isLoggedIn() {
        return getAuth0Tokens().isPresent();
    }

    public static Auth0Session getCurrent() {
        return (Auth0Session)VaadinSession.getCurrent();
    }


    public void login() {
        if(isLoggedIn()) {
            throw new IllegalStateException("User already logged in");
        }

        Page.getCurrent().open(LoginUI.getLoginURL(), null);
    }

    public void logout() {
        if(!isLoggedIn()) {
            throw new IllegalStateException("User not logged in");
        }

        // Generate URL to be called to logout session on Auth0
        final String logoutUrl = getAuthAPI().logoutUrl(getLogoutReturnUrl(), true)
                .withAccessToken(auth0Tokens.getAccessToken()).build();

        // Invalidate Vaadin session
        Auth0Session.getCurrent().close();

        // Jump to logout page to invalidate Auth0 session
        Page.getCurrent().open(logoutUrl, null);
    }

    public Optional<Auth0User> getUser() {
        return getAuth0UserInfo().map(i -> new Auth0User(i));
    }

    public static AuthAPI getAuthAPI() {
        Properties properties = getAuth0Properties();
        return new AuthAPI(properties.getProperty("auth0.domain"),
                properties.getProperty("auth0.clientId"),
                properties.getProperty("auth0.clientSecret"));
    }

    protected static Properties getAuth0Properties() {
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

    public static String getLogoutReturnUrl() {
        return getAuth0Properties().getProperty("auth0.logoutUrl");
    }
}
