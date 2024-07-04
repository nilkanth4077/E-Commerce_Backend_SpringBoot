package com.e_commerce.controller;

import com.e_commerce.entity.Cart;
import com.e_commerce.service.CartService;
import com.e_commerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;
import com.e_commerce.repository.UserRepo;
import com.e_commerce.dto.LoginRequest;
import com.e_commerce.dto.AuthResponse;
import com.e_commerce.service.JwtService;
import com.e_commerce.service.MyUserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepo userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private MyUserService myUserService;
    private CartService cartService;
    private UserService userService;


    public AuthController(UserRepo userRepository, PasswordEncoder passwordEncoder, MyUserService myUserService, JwtService jwtService, CartService cartService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserService = myUserService;
        this.jwtService = jwtService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String role = user.getRole();
        String mobile = user.getMobile();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new UserException("Email already exist");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setRole(role);
        createdUser.setMobile(mobile);
        createdUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(createdUser);
        Cart cart = cartService.createCart(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Signup Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token, "Login Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = myUserService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @GetMapping("/profile")
    public User getProfileByToken(@RequestHeader("Authorization") String jwt) throws UserException {
        return userService.getProfileByToken(jwt);
    }
}
