package org.GameExchange.ExchangeAPI;


import org.GameExchange.ExchangeAPI.Model.PersonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

//CustomAuthProvider
@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailAddr = authentication.getName();
        String password = authentication.getCredentials().toString();
        Authentication auth = null;
        try {
            // write your custom logic to match username, password
            boolean userExists = personJpaRepository.checkCreds(emailAddr, password);
            if (userExists) {
                auth = new UsernamePasswordAuthenticationToken(emailAddr, password);
            }
        } catch (Exception e) {

        }
        return auth;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}