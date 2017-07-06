package org.vaadin.alump.auth0demo;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Static utility class to access Auth0 configuration information
 */
class Auth0Util {

    private static Properties auth0Properties;

    public static AuthAPI getAuthAPI() {
        Properties properties = getAuth0Properties();
        return new AuthAPI(properties.getProperty("auth0.domain"),
                properties.getProperty("auth0.clientId"),
                properties.getProperty("auth0.clientSecret"));
    }

    public static Properties getAuth0Properties() {
        if(auth0Properties == null) {
            Properties properties = new Properties();

            try {
                InputStream stream = Auth0Util.class.getClassLoader().getResourceAsStream(
                        "auth0-default.properties");
                properties.load(stream);
            } catch(IOException e) {
                e.printStackTrace();
            }

            try {
                InputStream stream = Auth0Util.class.getClassLoader().getResourceAsStream("auth0.properties");
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

    /**
     * Get logout URL for access token
     * @param accessToken
     * @return
     */
    public static String getLogoutUrl(String accessToken) {
        return getAuthAPI().logoutUrl(getLogoutReturnUrl(), true)
                .withAccessToken(accessToken).build();
    }

    public static UserInfo resolveUser(String accessToken) {
        try {
            return getAuthAPI().userInfo(accessToken).execute();
        } catch(Auth0Exception e) {
            throw new IllegalStateException("Failed to resolve user for token", e);
        }
    }

    public static String getLoginURL() {
        return getAuth0Properties().getProperty("auth0.loginUrl");
    }

}
