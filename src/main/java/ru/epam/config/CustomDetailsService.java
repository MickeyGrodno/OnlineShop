package ru.epam.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.epam.models.User;
import ru.epam.service.user.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        User user = userService.loadUserByLogin(login);
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toUpperCase());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getLogin(), user.getPassword(), authorities);
        return userDetails;
    }
}
