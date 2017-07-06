package org.vaadin.alump.auth0demo;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alump on 05/07/2017.
 */
@SpringUI
@Theme("valo")
@Push
@Title("Auth0 Vaadin Demo")
public class Auth0DemoUI extends UI {

    @Autowired
    private SpringNavigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        navigator.init(this, this);
        setNavigator(navigator);
        navigator.setErrorView(MainView.class);
    }

    public static Auth0DemoUI getCurrent() {
        return (Auth0DemoUI)UI.getCurrent();
    }

}
