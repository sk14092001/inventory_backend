//package com.inventory_backend.inventory_backend.controller;
//
//import com.inventory_backend.inventory_backend.dto.LoginRequest;
//import com.inventory_backend.inventory_backend.dto.LoginResponse;
//import com.inventory_backend.inventory_backend.dto.SignUpRequest;
//import com.inventory_backend.inventory_backend.dto.SignUpResponse;
//import com.inventory_backend.inventory_backend.entity.Role;
//import com.inventory_backend.inventory_backend.entity.User;
//import com.inventory_backend.inventory_backend.repository.RoleRepository;
//import com.inventory_backend.inventory_backend.repository.UserRepository;
//import com.inventory_backend.inventory_backend.security.JwtUtil;
//import com.inventory_backend.inventory_backend.service.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
////    // ✅ Allowed roles for signup
////    private static final Set<String> ALLOWED_SIGNUP_ROLES = Set.of(
////            "ROLE_DEVELOPER",
////            "ROLE_TL"
////    );
//
//    @PostMapping("/signup")
//    public SignUpResponse signup(@RequestBody SignUpRequest request) {
//
//        // 1️⃣ Check email already exists
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already registered");
//        }
//
//        // 2️⃣ Validate & map roles
//        Set<Role> assignedRoles = request.getRoles().stream()
//                .map(role -> "ROLE_" + role.toUpperCase())
//                .map(roleName -> roleRepository.findByName(roleName)
//                        .orElseThrow(() ->
//                                new RuntimeException("Role not found: " + roleName)))
//                .collect(Collectors.toSet());
//
//        if (assignedRoles.isEmpty()) {
//            throw new RuntimeException("No valid roles provided");
//        }
//
//        // 3️⃣ Create user
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRoles(assignedRoles);
//
//        // 4️⃣ Save user
//        userRepository.save(user);
//
//        // 5️⃣ Response
//        SignUpResponse response = new SignUpResponse();
//        response.setEmail(user.getEmail());
//        response.setMessage("User registered successfully");
//
//        return response;
//    }
//
//
//
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request) {
//
//        // 🔐 Authenticate using email + password
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        // 🔍 Load user from DB
//        UserDetails userDetails =
//                userDetailsService.loadUserByUsername(request.getEmail());
//
//        // 🔑 Generate JWT
//        String token = jwtUtil.generateToken(userDetails);
//
//        // 📤 Build response
//        LoginResponse response = new LoginResponse();
//        response.setEmail(userDetails.getUsername());
//        response.setToken(token);
//        response.setRoles(
//                userDetails.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .toList()
//        );
//
//        return response;
//    }
//}
