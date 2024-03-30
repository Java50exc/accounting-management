package telran.probes;

import static telran.probes.api.AccountingValidationErrorMessages.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.AccountDto;
import telran.probes.dto.PasswordUpdateData;
import telran.probes.model.Account;

@Component
@RequiredArgsConstructor
public class TestDb {
	final MongoTemplate mongoTemplate;
	
	static final String EMAIL1 = "email1@gmail.com";
	static final String EMAIL2 = "email2@gmail.com";
	static final String WRONG_EMAIL = "email1";
	static final String EMPTY_EMAIL = "";
	
	static final String SHORT_PASSWORD = "123";
	static final String PASSWORD_WHITESPACE = "123 45678";
	static final String PASSWORD_EMPTY = "";
	static final String PASSWORD1 = "12345678";
	static final String PASSWORD2 = "12345578";
	
	static final String ROLE1 = "Role1";
	static final String ROLE2 = "Role2";
	
	static final String[] ROLES1 = { ROLE1 };
	static final String[] ROLES2 = { ROLE2 };
	static final String[] ROLES = { ROLE1, ROLE2 };
	static final String[] ROLES_EMPTY = {};	
	
	static final String PATH = "http://localhost:8080";

	static final AccountDto ACCOUNT_DTO1 = new AccountDto(EMAIL1, PASSWORD1, ROLES1);
	static final AccountDto ACCOUNT_DTO2 = new AccountDto(EMAIL2, PASSWORD2, ROLES2);
	static final AccountDto ACCOUNT_DTO = new AccountDto(EMAIL1, PASSWORD1, ROLES);
	static final AccountDto ACCOUNT_WRONG_EMAIL = new AccountDto(WRONG_EMAIL, PASSWORD1, ROLES);
	static final AccountDto ACCOUNT_NULL_EMAIL = new AccountDto(null, PASSWORD1, ROLES);
	static final AccountDto ACCOUNT_EMPTY_EMAIL = new AccountDto(EMPTY_EMAIL, PASSWORD1, ROLES);
	static final AccountDto ACCOUNT_SHORT_PASSWORD = new AccountDto(EMAIL1, SHORT_PASSWORD, ROLES);
	static final AccountDto ACCOUNT_WHITESPACES_PASSWORD = new AccountDto(EMAIL1, PASSWORD_WHITESPACE, ROLES);
	static final AccountDto ACCOUNT_EMPTY_PASSWORD = new AccountDto(EMAIL1, PASSWORD_EMPTY, ROLES);
	static final AccountDto ACCOUNT_NULL_PASSWORD = new AccountDto(EMAIL1, null, ROLES);
	static final AccountDto ACCOUNT_EMPTY_ROLES = new AccountDto(EMAIL1, PASSWORD1, ROLES_EMPTY);
	static final AccountDto ACCOUNT_NULL_ROLES = new AccountDto(EMAIL1, PASSWORD1, null);
	static final AccountDto ACCOUNT_ALL_NULL = new AccountDto(null, null, null);
	static final PasswordUpdateData PASSWORD_UPDATE_DTO = new PasswordUpdateData(EMAIL1, PASSWORD1);
	
	static final Account ACCOUNT = Account.builder().email(EMAIL1).hashPassword(PASSWORD1).roles(ROLES1).build();
	
	static final String[] MISSING_MESSAGES = { EMPTY_EMAIL_MSG, EMPTY_PASSWORD, EMPTY_ROLES };
	
	
	public void initDb() {
		mongoTemplate.findAllAndRemove(new Query(), ACCOUNT.getClass());
		mongoTemplate.insert(ACCOUNT);
	}
	
	public Account findAccount(String email) {
		return mongoTemplate.findById(email, ACCOUNT.getClass());
	}
	
	public long getCount() {
		return mongoTemplate.count(new Query(), ACCOUNT.getClass());
	}

}
