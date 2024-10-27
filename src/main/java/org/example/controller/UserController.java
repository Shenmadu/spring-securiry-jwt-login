package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.AuthRequest;
import org.example.entity.UserInfo;
import org.example.service.JwtService;
import org.example.service.impl.UserInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    final UserInfoService userInfoService;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;


    @PostMapping("/addNewUser")
    public String registerUser(@RequestBody UserInfo userInfo){
        return userInfoService.addUser(userInfo);

    }
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            List<String> roles = userInfoService.getUserRoles(authRequest.getUsername());
            return jwtService.generateToken(authRequest.getUsername(),roles);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

}

