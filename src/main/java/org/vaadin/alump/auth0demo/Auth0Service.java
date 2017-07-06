package org.vaadin.alump.auth0demo;

import com.vaadin.server.*;
import com.vaadin.spring.server.SpringVaadinServletService;

import java.util.List;

/**
 * Created by alump on 05/07/2017.
 */
public class Auth0Service extends SpringVaadinServletService {

    public Auth0Service(VaadinServlet servlet,
                                      DeploymentConfiguration deploymentConfiguration, String serviceUrl)
            throws ServiceException {

        super(servlet, deploymentConfiguration, serviceUrl);

    }

    @Override
    protected List<RequestHandler> createRequestHandlers()
            throws ServiceException {

        List<RequestHandler> list = super.createRequestHandlers();

        return list;
    }

    @Override
    protected VaadinSession createVaadinSession(VaadinRequest request)
            throws ServiceException {
        return new Auth0Session(this);
    }
}
