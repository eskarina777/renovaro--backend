package eu.renovaro.auth.service.impl;

import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import eu.renovaro.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        private final UserRepository userRepository;
        private final UserRoleRepository userRoleRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            log.info("Invoking loadUserByUsername for: {}", username);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Not valid username or password"));

            Set<GrantedAuthority> authorities = userRoleRepository.findAllByUserId(user.getUserId()).stream()
                    .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName().toString()))
                    .collect(Collectors.toSet());
            log.info("Authorities: {}", authorities);
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),user.getPassword(),authorities
            );
        }
    }