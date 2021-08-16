package com.example.demo.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.exception.AuthenticateException;
import com.example.demo.exception.RegraNegocioException;
import com.example.demo.model.entity.User;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserServiceTest {

	@MockBean
	UserRepository repository;
	
	@SpyBean
	UserServiceImpl service;
	
	@Test
	public void deveValidarQueUmEmailEstaDisponivelParaUso() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		String email = "aa@email.com";
		service.validateEmail(email);		
	}

	@Test
	public void deveLancarErroAoValidarIndisponibilidadeDoEmailInformado() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação
		String email = "aa@email.com";
		
		Throwable exception = Assertions.catchThrowable(() -> service.validateEmail(email));
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class);				
	}	

	@Test
	public void deveSalvarUmUsuarioComSucesso() {
		//cenário
		User user = retornUsuario();
		user.setId(1L);
		
		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
		Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
		
		//ação
		service.saveUser(new User());
		
		//verificação
		Assertions.assertThat(user.getId()).isNotNull();						
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioPorFalhaValidacaoEmail() {
		//cenário
		User user = retornUsuario();		
		Mockito.doThrow(RegraNegocioException.class).when(service).validateEmail(Mockito.anyString());
		
		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.saveUser(user) );
				
		//verificação
		Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class);						
	}
	
	@Test
	public void deveLancarErroQuandoNaoLocalizarUsuarioNaAutenticacao() {
		//cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate("ee@email.com", "1234"));
		
		//verificação		
		Assertions.assertThat(exception).isInstanceOf(AuthenticateException.class);		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaInformadaForInvalida() {
		//cenário
		User user = retornUsuario();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
		
		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.authenticate("a@email.com", "123456"));
		
		//verificação
		Assertions.assertThat(exception).isInstanceOf(AuthenticateException.class).hasMessage("Senha inválida.");		
	}
	
	@Test
	public void deveAutenticarUsuarioComSucesso() {
		//cenário
		User user = retornUsuario();
		user.setId(1L);
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
		
		//ação
		User userAutenticado = service.authenticate("a@email.com", "123");
		
		//verificação
		Assertions.assertThat(userAutenticado.getId()).isNotNull();
	}
	
	public User retornUsuario() {		
		return User.builder().name("aa").email("aa@email.com").password("123").build();
	}
}
