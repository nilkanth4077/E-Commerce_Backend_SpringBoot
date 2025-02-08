package com.e_commerce.controller;

import com.e_commerce.service.*;
import jakarta.mail.MessagingException;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepo userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MyUserService myUserService;
    private final CartService cartService;
    private final UserService userService;

    public AuthController(UserRepo userRepository, EmailService emailService, PasswordEncoder passwordEncoder, MyUserService myUserService, JwtService jwtService, CartService cartService, UserService userService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.myUserService = myUserService;
        this.jwtService = jwtService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUserHandler(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
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
        createdUser.setRole("USER");
        createdUser.setMobile(mobile);
        createdUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(createdUser);
        cartService.createCart(savedUser);

        String emailBody = String.format(
                "Dear " + savedUser.getFirstName() + ",\n\n" +
                        "Your registration is successful. Here are the details:\n\n" +
                        "Full name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n\n" +
                        "Thank you for registering with us. If you have any questions or need further assistance, please do not hesitate to contact us.\n\n" +
                        "Best regards,\n" +
                        "From Ecommerce Service Provider"
        );
        emailService.sendSimpleMessage(savedUser.getEmail(), "Signup", emailBody);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        User user = userRepository.findByEmail(authentication.getName());

        AuthResponse authResponse = new AuthResponse(token, "Login Success", user.getRole(), user);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String username, String password) {
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

    @PostMapping("/email")
    public String submitContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message) throws MessagingException {
        emailService.sendUserDetails(name, email, subject, message);
        return "Thank you for contacting us! Your message has been received. We will get back to you soon.";
    }
}
