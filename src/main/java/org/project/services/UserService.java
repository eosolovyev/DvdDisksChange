package org.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service//Сервис пользователя, создется для того, чтобы спринг знал откуда этих пользователей брать
public class UserService implements UserDetailsService {

    @Autowired
    private Repo repo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //Ищет из репозитория
        UserDetails userDetails = repo.findByUsername(s);
        if (userDetails == null) {
            //Если не нашел, то выбрасывает исключения
            throw new UsernameNotFoundException("no user");
        }
        return userDetails;
    }
}
