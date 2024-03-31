package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static telran.probes.TestDb.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import telran.probes.exceptions.*;
import telran.probes.model.Account;
import telran.probes.service.AccountingManagementService;

@SpringBootTest
class AccountingManagementServiceTests {
	@Autowired
	AccountingManagementService accountingService;
	@Autowired
	TestDb testDb;
	@Autowired
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		testDb.initDb();
	}

	@Test
	void addAccount_correctFlow_success() {
		assertNull(testDb.findAccount(EMAIL2));
		assertEquals(ACCOUNT_DTO2, accountingService.addAccount(ACCOUNT_DTO2));
		Account account = testDb.findAccount(EMAIL2);
		assertEquals(ACCOUNT_DTO2, account.toDto());
		assertTrue(passwordEncoder.matches(PASSWORD2, account.getHashPassword()));
	}
	
	@Test
	@WithMockUser(EMAIL1)
	void removeAccount_correctFlow_success() {
		assertNotNull(testDb.findAccount(EMAIL1));
		assertEquals(ACCOUNT_DTO1, accountingService.removeAccount(EMAIL1));
		assertNull(testDb.findAccount(EMAIL1));
	}
	
	@Test
	@WithMockUser(EMAIL1)
	void updatePassword_correctFlow_success() {
		Account account = testDb.findAccount(EMAIL1);
		accountingService.updatePassword(EMAIL1, PASSWORD2);
		Account accountUpdated = testDb.findAccount(EMAIL1);
		assertNotEquals(account.getHashPassword(), accountUpdated.getHashPassword());
		assertTrue(passwordEncoder.matches(PASSWORD2, accountUpdated.getHashPassword()));
		assertFalse(passwordEncoder.matches(PASSWORD1, accountUpdated.getHashPassword()));
	}
	
	@Test
	void addAccount_alreadyExists_throwsException() {
		long count = testDb.getCount();
		Account account = testDb.findAccount(EMAIL1);
		assertThrowsExactly(AccountIllegalStateException.class, () -> accountingService.addAccount(ACCOUNT_DTO1));
		assertEquals(account, testDb.findAccount(EMAIL1));
		assertEquals(count, testDb.getCount());
	}
	
	@Test
	@WithMockUser(EMAIL2)
	void removeAccount_notFound_throwsException() {
		long count = testDb.getCount();
		assertThrowsExactly(AccountNotFoundException.class, () -> accountingService.removeAccount(EMAIL2));
		assertEquals(count, testDb.getCount());
	}
	
	@Test
	@WithMockUser(EMAIL2)
	void updatePassword_forbidden_throwsException() {
		Account account = testDb.findAccount(EMAIL1);
		assertThrowsExactly(AccessDeniedException.class, () -> accountingService.updatePassword(EMAIL1, PASSWORD2));
		Account accountAfter = testDb.findAccount(EMAIL1);
		assertEquals(account.getHashPassword(), accountAfter.getHashPassword());
		assertTrue(passwordEncoder.matches(PASSWORD1, accountAfter.getHashPassword()));
		assertFalse(passwordEncoder.matches(PASSWORD2, accountAfter.getHashPassword()));
	}

}
