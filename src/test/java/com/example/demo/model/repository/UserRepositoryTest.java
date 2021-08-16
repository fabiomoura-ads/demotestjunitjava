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
		String nome = "fulano";
		String email = "fulano@email.com";
		String senha = "123456";
		User user = User.builder().name(nome).email(email).password(senha).build();		
		entityManager.persist(user);
		
		//ação
		boolean exists = repository.existsByEmail(email);
		
		//verificação
		Assertions.assertTrue(exists);
		
	}
	
	@Test
	public void naoDeveEncontrarUmUsuarioPorEmailInformado() {
		//cenário
		String email = "fulano@email.com";
		User user = User.builder().name("fulano").email(email).password("123456").build();	
		entityManager.persist(user);
		
		//ação
		boolean exists = repository.existsByEmail("sicrano@email.com");
		
		//verificaçãot
		Assertions.assertFalse(exists);
		
	}	
}
