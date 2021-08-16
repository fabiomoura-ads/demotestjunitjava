package com.example.demo.service;

import com.example.demo.model.entity.User;

public interface UserService {

	User authenticate(String email, String password);

	User saveUser(User user);

	void validateEmail(String email);

}
