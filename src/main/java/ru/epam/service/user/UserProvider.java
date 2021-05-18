package ru.epam.service.user;

public interface UserProvider {
    String getUsername();
    boolean isAuthenticated();
}
