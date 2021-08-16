package com.example.demo.model.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.entity.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	UserRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveEncontrarUmUsuarioPorEmailInformado() {
		//cenário
		String email = "aa@email.com";
		User user = User.builder().name("bb").email(email).password("123").build();		
		entityManager.persist(user);
		
		//ação
		boolean exists = repository.existsByEmail(email);
		
		//verificação
		Assertions.assertTrue(exists);
		
	}
	
	@Test
	public void naoDeveEncontrarUmUsuarioPorEmailInformado() {
		//cenário
		String email = "aa@email.com";
		User user = User.builder().name("bb").email(email).password("123").build();	
		entityManager.persist(user);
		
		//ação
		boolean exists = repository.existsByEmail("bb@email.com");
		
		//verificaçãot
		Assertions.assertFalse(exists);
		
	}	
}
