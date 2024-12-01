package org.wsb.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class AuthenticationHelper {
    public static String getUser() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication().isAuthenticated()) {
            return (String) context.getAuthentication().getPrincipal();
        }
        throw new RuntimeException();
    }

    public Collection<String> getAuthorities() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication().isAuthenticated()) {
            return context.getAuthentication().getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).toList();
        }
        throw new RuntimeException();
    }
}
