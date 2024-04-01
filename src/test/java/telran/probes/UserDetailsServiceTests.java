package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static telran.probes.TestDb.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.*;
import telran.probes.repo.AccountRepository;

@SpringBootTest
public class UserDetailsServiceTests {
	@Autowired
	TestDb testDb;
	@MockBean
	AccountRepository accountRepo;
	@Autowired
	UserDetailsService userService;
	
	
	@Test
	void loadUserByUserName_userExists_success() {
		when(accountRepo.findById(any())).thenReturn(Optional.of(ACCOUNT));
		UserDetails user = userService.loadUserByUsername(EMAIL1);
		assertEquals(ACCOUNT.getEmail(), user.getUsername());
		assertEquals(ACCOUNT.getHashPassword(), user.getPassword());
		assertTrue(user.isEnabled());
	}
	
	@Test
	void loadUserByUserName_userNotExists_throwsException() {
		when(accountRepo.findById(any())).thenReturn(Optional.empty());
		assertThrowsExactly(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL1));
	}

}
