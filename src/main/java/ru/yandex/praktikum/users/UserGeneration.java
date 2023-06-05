package ru.yandex.praktikum.users;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Locale;

public class UserGeneration {
    private final String rndLogin;
    private final String rndPassword;
    private final String rndRequestName;
    private final String rndEmail;
    private final String rndName;

    public UserGeneration() {
        rndEmail = RandomStringUtils.randomAlphabetic(7).toLowerCase(Locale.ROOT) + "@gmail.ru";
        rndName = RandomStringUtils.randomAlphabetic(6);
        rndLogin = "\"email\": \"" + rndEmail;
        rndRequestName = "\"name\": \"" + rndName;
        rndPassword = "\"password\": \"" + RandomStringUtils.randomAlphabetic(8);
    }

    public String getRndName() {
        return rndName;
    }

    public String getEmail() {
        return rndEmail;
    }

    public String getPassword() {
        return rndPassword;
    }

    public String getName() {
        return rndRequestName;
    }

    public String getLogin() {
        return rndLogin;
    }
}
