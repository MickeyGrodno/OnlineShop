package ru.epam.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.epam.models.User;
import ru.epam.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(login);
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toUpperCase());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        return new org.springframework.security.core.userdetails.
                User(user.getLogin(), user.getPassword(), authorities);
    }
}
