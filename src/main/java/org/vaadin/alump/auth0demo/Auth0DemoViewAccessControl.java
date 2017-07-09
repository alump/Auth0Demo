package org.vaadin.alump.auth0demo;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

/**
 * Example of view access control
 */
@Component
public class Auth0DemoViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        Auth0Session session = (Auth0Session)ui.getSession();

        //System.out.println("Access check for " + beanName);

        // Just a simple example, do not allow user to access limited view unless she has logged in
        if(!session.isLoggedIn() && beanName.equals(LimitedView.VIEW_NAME)) {
            return false;
        }

        return true;
    }
}
