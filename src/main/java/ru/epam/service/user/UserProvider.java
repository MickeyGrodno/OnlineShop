package ru.epam.service.user;

public interface UserProvider {
    String getUserName();
    boolean isAuthenticated();
    String getUserRole();
}
