package com.e_commerce.service;

import com.e_commerce.repository.UserRepo;
import org.springframework.stereotype.Service;

import com.e_commerce.entity.User;
import com.e_commerce.exception.UserException;

import java.util.Optional;

@Service
public class UserService {

	private UserRepo userRepo;
	private JwtService jwtService;

	public UserService(UserRepo userRepo, JwtService jwtService){
		this.userRepo = userRepo;
		this.jwtService = jwtService;
	}

    public User findCustomUserById(Long userId) throws UserException {
		Optional<User> user = userRepo.findById(userId);
		if(user.isPresent()){
			return user.get();
		}
        throw new UserException("User not found with id: " + userId);
    }

    public User findCustomUserProfileByToken(String token) throws UserException {
		String email = jwtService.extractEmail(token);

		User user = userRepo.findByEmail(email);
		if(user == null){
			throw new UserException("User not found with tmail: " + email);
		}
        return user;
    }

}
