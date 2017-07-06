package org.vaadin.alump.auth0demo;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by alump on 7/6/17.
 */
@Service
public class Auth0Management {

    public Auth0Management() {

    }

    public boolean isEnabled() {
        String key = Auth0Util.getAuth0Properties().getProperty("auth0.managementToken");
        return key != null && !key.isEmpty();
    }

    protected ManagementAPI getAPI() {
        Properties properties = Auth0Util.getAuth0Properties();
        ManagementAPI api = new ManagementAPI(properties.getProperty("auth0.domain"),
                properties.getProperty("auth0.managementToken"));
        return api;
    }

    public List<User> getUsers() throws Auth0Exception {
        UsersPage users = getAPI().users().list(null).execute();
        return users.getItems();
    }

    public Optional<User> getUser(String userId) throws Auth0Exception {
        return Optional.ofNullable(getAPI().users().get(userId, null).execute());
    }

    public void setUserBlocked(Auth0User user, boolean blocked) throws Auth0Exception {
        User entity = getAPI().users().get(user.getUserId(), null).execute();
        entity.setBlocked(blocked);
        getAPI().users().update(user.getUserId(), entity);
    }

    public void removeUser(Auth0User user) throws Auth0Exception {
        getAPI().users().delete(user.getUserId()).execute();
    }
}
