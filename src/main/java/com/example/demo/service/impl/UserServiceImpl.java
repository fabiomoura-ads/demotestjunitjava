package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.exception.AuthenticateException;
import com.example.demo.exception.RegraNegocioException;
import com.example.demo.model.entity.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	UserRepository repository;
	
	public UserServiceImpl(UserRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public User authenticate(String email, String password) {
		Optional<User> result = repository.findByEmail(email);
		
		if ( result.isEmpty() ) {
			throw new AuthenticateException("Usuário não localizado pelo email informado.");
		}
		
		if ( !result.get().getPassword().equals(password) ) {
			throw new AuthenticateException("Senha inválida.");
		}
		
		return result.get();
	}

	@Override
	public User saveUser(User user) {
		validateEmail(user.getEmail());
		return repository.save(user);
	}

	@Override
	public void validateEmail(String email) {
		boolean exists = repository.existsByEmail(email);
		if ( exists ) {
			throw new RegraNegocioException("O email já está sendo utilizado por outro usuário.");
		}
		
	}

}
