//package com.passwordwallet.service;
//
//import com.passwordwallet.dao.UserRepository;
//import com.passwordwallet.entity.UserEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepository usersRepository;
//
//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        Optional<UserEntity> optionalUser = usersRepository.findByLogin(login);
//        if(optionalUser.isPresent()) {
//            UserEntity users = optionalUser.get();
//
//            return User.builder()
//                    .username(users.getLogin())
//                    //change here to store encoded password in db
//                    .password(bCryptPasswordEncoder.encode(users.getPasswordHash()))
//                    .roles("USER")
//                    .build();
//        } else {
//            throw new UsernameNotFoundException("User Name is not Found");
//        }
//    }
//}
