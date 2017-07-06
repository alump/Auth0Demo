package org.vaadin.alump.auth0demo;

import com.auth0.json.auth.UserInfo;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class Auth0User {

    private final UserInfo userInfo;

    Auth0User(UserInfo info) {
        this.userInfo = Objects.requireNonNull(info);
    }

    public String getValue(String key) {
        return getValue(key, String.class);
    }

    public <T extends Object> T getValue(String key, Class<T> castTo) {
        return castTo.cast(userInfo.getValues().get(key));
    }

    public Optional<String> getOptionalValue(String key) {
        return getOptionalValue(key, String.class);
    }

    public <T extends Object> Optional<T> getOptionalValue(String key, Class<T> castTo) {
        return Optional.ofNullable(userInfo.getValues().get(key)).map(v -> castTo.cast(v));
    }

    public String getName() {
        return getValue("name");
    }

    public String getEmail() {
        return getValue("email");
    }

    public Optional<Boolean> isEmailVerified() {
        return getOptionalValue("email_verified", Boolean.class);
    }

    public Optional<String> getGender() {
        return getOptionalValue("gender");
    }

    public Optional<String> getPicture() {
        return getOptionalValue("picture");
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(getValue("locale"));
    }

    public String getUserId() {
        return getValue("user_id");
    }

    public Optional<String> getGivenName() {
        return getOptionalValue("given_name");
    }

    public Optional<String> getFamilyName() {
        return getOptionalValue("family_name");
    }
}
