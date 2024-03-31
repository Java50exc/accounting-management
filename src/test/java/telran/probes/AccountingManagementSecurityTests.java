package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.TestDb.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.probes.auth.UserDetailsServiceImpl;
import telran.probes.configuration.SecurityConfiguration;
import telran.probes.service.AccountingManagementService;
import telran.probes.dto.*;
import telran.probes.repo.AccountRepository;

@WebMvcTest
@Import(SecurityConfiguration.class)
public class AccountingManagementSecurityTests {
	@Value("${app.accounts.api.path}")
	String ACCOUNTS_PATH;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	AccountingManagementService accountService;
	@MockBean
	AccountRepository accountRepo;
	@Autowired
	UserDetailsService service;

	@TestConfiguration
	class TestConfig {
		@Bean
		UserDetailsService userDetailsService() {
			return new UserDetailsServiceImpl(accountRepo);
		}
	}

	//correct flow
	@Test
	@WithMockUser(roles = "ADMIN")
	void addAccount_admin_success() throws Exception {
		when(accountService.addAccount(any(AccountDto.class))).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(post(ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(ACCOUNT_DTO))).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles = "USER")
	void removeAccount_user_success() throws Exception {
		when(accountService.removeAccount(any())).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(delete(ACCOUNTS_PATH + "/" + EMAIL1)).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void updatePassword_authorized_success() throws Exception {
		doNothing().when(accountService).updatePassword(anyString(), anyString());
		mockMvc.perform(put(ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(PASSWORD_UPDATE_DTO))).andExpect(status().isOk());
	}
	
	
	//forbidden
	@Test
	@WithMockUser(roles = "USER")
	void addAccount_user_403() throws Exception {
		when(accountService.addAccount(any(AccountDto.class))).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(post(ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(ACCOUNT_DTO))).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void removeAccount_admin_403() throws Exception {
		when(accountService.removeAccount(any())).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(delete(ACCOUNTS_PATH + "/" + EMAIL1)).andExpect(status().isForbidden());
	}
	
	
	//unauthorized
	@Test
	@WithAnonymousUser
	void addAccount_unauthorized_401() throws Exception {
		when(accountService.addAccount(any(AccountDto.class))).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(post(ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(ACCOUNT_DTO))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithAnonymousUser
	void removeAccount_unauthorized_401() throws Exception {
		when(accountService.removeAccount(any())).thenReturn(ACCOUNT_DTO);
		mockMvc.perform(delete(ACCOUNTS_PATH + "/" + EMAIL1)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithAnonymousUser
	void updatePassword_unauthorized_401() throws Exception {
		doNothing().when(accountService).updatePassword(anyString(), anyString());
		mockMvc.perform(put(ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(PASSWORD_UPDATE_DTO))).andExpect(status().isUnauthorized());
	}
	
	

	//UserDetailsService tests
	@Test
	void loadUserByUserName_userExists_success() {
		when(accountRepo.findById(any())).thenReturn(Optional.of(ACCOUNT));
		UserDetails user = service.loadUserByUsername(EMAIL1);
		assertEquals(ACCOUNT.getEmail(), user.getUsername());
		assertEquals(ACCOUNT.getHashPassword(), user.getPassword());
		assertTrue(user.isEnabled());
	}
	
	@Test
	void loadUserByUserName_userNotExists_throwsException() {
		when(accountRepo.findById(any())).thenReturn(Optional.empty());
		assertThrowsExactly(UsernameNotFoundException.class, () -> service.loadUserByUsername(EMAIL1));
	}

}
