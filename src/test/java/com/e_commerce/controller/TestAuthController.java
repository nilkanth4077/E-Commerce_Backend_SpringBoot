package com.e_commerce.controller;

import com.e_commerce.dto.AuthResponse;
import com.e_commerce.dto.LoginRequest;
import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;
import com.e_commerce.repository.UserRepo;
import com.e_commerce.service.CartService;
import com.e_commerce.service.JwtService;
import com.e_commerce.service.MyUserService;
import com.e_commerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TestAuthController {

    private MockMvc mockMvc;

    @Mock
    private UserRepo userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MyUserService myUserService;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication auth;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        jwtService = new JwtService();
    }

//    @Test
//    public void isTokenGenerated() {
//        Mockito.when(auth.getName()).thenReturn("test@gmail.com");
//        String token = jwtService.generateToken(auth);
//        System.out.println("Token: " + token);
//        assertNotNull(token);
//    }

    @Test
    public void testCreateUserHandler_Success() throws UserException {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setRole("ROLE_USER");
        newUser.setMobile("1234567890");
        newUser.setCreatedAt(LocalDateTime.now());
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<AuthResponse> responseEntity = authController.createUserHandler(newUser);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Signup Success", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getToken());
        assertEquals("mockedToken", responseEntity.getBody().getToken());
    }

//    @Test
//    public void testCreateUserHandler_DuplicateEmail() {
//        User existingUser = new User();
//        existingUser.setEmail("test@example.com");
//        existingUser.setPassword("password");
//        existingUser.setFirstName("John");
//        existingUser.setLastName("Doe");
//        existingUser.setRole("ROLE_USER");
//        existingUser.setMobile("1234567890");
//        existingUser.setCreatedAt(LocalDateTime.now());
//        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);
//
//        assertThrows(UserException.class, () -> authController.createUserHandler(existingUser));
//    }
//
//    @Test
//    public void testLoginUserHandler_Success() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("password");
//
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setRole("ROLE_USER");
//        user.setMobile("1234567890");
//        user.setCreatedAt(LocalDateTime.now());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
//        when(authController.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(authentication);
//        when(jwtService.generateToken(authentication)).thenReturn("dummyToken");
//
//        ResponseEntity<AuthResponse> responseEntity = authController.loginUserHandler(loginRequest);
//
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
//        assertEquals("Login Success", responseEntity.getBody().getMessage());
//        assertNotNull(responseEntity.getBody().getToken());
//    }
//
//    @Test
//    public void testLoginUserHandler_BadCredentials() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("wrongPassword");
//        when(authController.authenticate(loginRequest.getEmail(), loginRequest.getPassword()))
//                .thenThrow(new BadCredentialsException("Invalid Password"));
//
//        assertThrows(BadCredentialsException.class, () -> authController.loginUserHandler(loginRequest));
//    }

}
