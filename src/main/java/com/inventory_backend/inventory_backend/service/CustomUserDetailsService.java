//package com.inventory_backend.inventory_backend.service;
//
//import com.inventory_backend.inventory_backend.entity.User;
//import com.inventory_backend.inventory_backend.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
//
//        List<SimpleGrantedAuthority> authorities =
//                user.getRoles().stream()
//                        .map(role -> new SimpleGrantedAuthority(role.getName()))
//                        .toList();
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),   // 🔥 important
//                user.getPassword(),
//                authorities
//        );
//    }
//}