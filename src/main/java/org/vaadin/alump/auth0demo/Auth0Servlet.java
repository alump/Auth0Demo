package org.vaadin.alump.auth0demo;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.spring.server.SpringVaadinServletService;
import org.springframework.stereotype.Component;

@Component("vaadinServlet")
public class Auth0Servlet extends SpringVaadinServlet {

    @Override
    protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        // this is needed when using a custom service URL
        Auth0Service service = new Auth0Service(
                this, deploymentConfiguration, getServiceUrlPath());
        service.init();
        return service;
    }
}
