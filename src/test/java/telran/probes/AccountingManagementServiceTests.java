package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static telran.probes.TestDb.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.probes.exceptions.*;
import telran.probes.model.Account;
import telran.probes.service.AccountingManagementService;

@SpringBootTest
class AccountingManagementServiceTests {
	@Autowired
	AccountingManagementService accountingService;
	@Autowired
	TestDb testDb;
	
	@Test
	void loadContext() {
		
	}

	@BeforeEach
	void setUp() {
		testDb.initDb();
	}

	@Test
	void addAccount_correctFlow_success() {
		assertNull(testDb.findAccount(EMAIL2));
		assertEquals(ACCOUNT_DTO2, accountingService.addAccount(ACCOUNT_DTO2));
		assertEquals(ACCOUNT_DTO2, testDb.findAccount(EMAIL2).toDto());
	}
	
	@Test
	void removeAccount_correctFlow_success() {
		assertNotNull(testDb.findAccount(EMAIL1));
		assertEquals(ACCOUNT_DTO1, accountingService.removeAccount(EMAIL1));
		assertNull(testDb.findAccount(EMAIL1));
	}
	
	@Test
	void addAccount_alreadyExists_throwsException() {
		long count = testDb.getCount();
		Account acc = testDb.findAccount(EMAIL1);
		assertThrowsExactly(AccountIllegalStateException.class, () -> accountingService.addAccount(ACCOUNT_DTO1));
		assertEquals(acc, testDb.findAccount(EMAIL1));
		assertEquals(count, testDb.getCount());
	}
	
	@Test
	void removeAccount_notFound_throwsException() {
		long count = testDb.getCount();
		System.out.println(count);
		assertThrowsExactly(AccountNotFoundException.class, () -> accountingService.removeAccount(EMAIL2));
		assertEquals(count, testDb.getCount());
	}

}
