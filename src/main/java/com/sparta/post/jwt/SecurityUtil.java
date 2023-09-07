package com.sparta.post.jwt;

import com.sparta.post.entity.User;
import com.sparta.post.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {
    public static Optional<User> getPrincipal() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserDetailsImpl.class::cast)
                .map(UserDetailsImpl::getUser);
    }
}
