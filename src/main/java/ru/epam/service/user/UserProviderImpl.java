package ru.epam.service.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserProviderImpl implements UserProvider {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return "UNKNOWN";
        }
        return authentication.getName();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser") || Objects.isNull(authentication)) {
            return false;
        } else {
            return true;
        }
    }

    public String getUserRole() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();
    }
}
