package com.example.registrationandlogin.service;

import com.example.registrationandlogin.model.MyUserDetails;
import com.example.registrationandlogin.model.Role;
import com.example.registrationandlogin.model.User;
import com.example.registrationandlogin.repository.UserRepository;
import com.example.registrationandlogin.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserRegistrationDto userRegistrationDto) {
        /*User user = new User(userRegistrationDto.getFirstName(), userRegistrationDto.getLastName(), userRegistrationDto.getEmail(), userRegistrationDto.getPassword(), Arrays.asList(new Role("ROLE_USER")));*/
        User user = new User(userRegistrationDto.getFirstName(), userRegistrationDto.getLastName(), userRegistrationDto.getEmail(), passwordEncoder.encode(userRegistrationDto.getPassword()), Collections.singletonList(new Role("ROLE_USER")));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("No user with email: " + email);
        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        return new MyUserDetails(user);
    }

    /*private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }*/


}
