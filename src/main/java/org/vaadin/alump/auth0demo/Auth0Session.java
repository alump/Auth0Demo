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

        Page.getCurrent().open(Auth0Util.getLoginURL(), null);
    }

    public void logout() {
        if(!isLoggedIn()) {
            throw new IllegalStateException("User not logged in");
        }

        String logoutUrl = Auth0Util.getLogoutUrl(auth0Tokens.getAccessToken());

        // Invalidate Vaadin session, clear tokens
        close();
        auth0Tokens = null;
        auth0UserInfo = null;

        // Jump to logout page to invalidate Auth0 session
        Page.getCurrent().open(logoutUrl, null);
    }

    public Optional<Auth0User> getUser() {
        return getAuth0UserInfo().map(i -> new Auth0User(i));
    }


}
