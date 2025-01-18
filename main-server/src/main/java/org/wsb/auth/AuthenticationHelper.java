package org.wsb.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticationHelper {
    public static String getUser() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication().isAuthenticated()) {
            var userDetails = (UserDetails) context.getAuthentication().getPrincipal();
            return (String) userDetails.getUsername();
        }
        throw new RuntimeException();
    }

    public static Collection<String> getAuthorities() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication().isAuthenticated()) {
            return context.getAuthentication().getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).toList();
        }
        throw new RuntimeException();
    }
}
