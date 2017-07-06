package org.vaadin.alump.auth0demo;

import com.auth0.*;
import com.auth0.json.auth.UserInfo;
import com.vaadin.server.*;

import java.util.Optional;


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

        userInfo.getValues().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
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

        Auth0Session.getCurrent().close();
        Page.getCurrent().open(LoginUI.getLoginURL(), null);
    }

    public Optional<Auth0User> getUser() {
        return getAuth0UserInfo().map(i -> new Auth0User(i));
    }
}
