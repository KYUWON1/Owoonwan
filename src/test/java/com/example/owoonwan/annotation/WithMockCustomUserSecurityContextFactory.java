package com.example.owoonwan.annotation;

import com.example.owoonwan.type.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        annotation.username()
                        ,annotation.password()
                        ,
                        Collections.singletonList(new SimpleGrantedAuthority(UserRole.USER.name())
                ));
        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }
}
