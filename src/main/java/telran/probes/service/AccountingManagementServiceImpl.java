package telran.probes.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.configuration.SecurityConfiguration;
import telran.probes.dto.AccountDto;
import telran.probes.exceptions.*;
import telran.probes.model.Account;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountingManagementServiceImpl implements AccountingManagementService {
	final MongoTemplate mongoTemplate;
	final PasswordEncoder passwordEncoder;

	@Override
	public AccountDto addAccount(AccountDto account) {
		Account accountDoc = Account.builder()
				.email(account.email())
				.hashPassword(passwordEncoder.encode(account.password()))
				.roles(account.roles())
				.passLength(account.password().length())
				.build();
		log.debug("Service: addAccount: Account created {} with encoded password", accountDoc);
		try {
			mongoTemplate.insert(accountDoc);
			log.debug("Service: addAccount: Account {} succesfully saved to db", accountDoc);
		} catch (DuplicateKeyException e) {
			log.error("Service: addAccount: Account with email {} already exists", accountDoc.getEmail());
			throw new AccountIllegalStateException();
		}
		AccountDto accountRes = accountDoc.toDto();
		log.trace("Service: addAccount: returns dto {}", accountRes);
		return accountRes;
	}

	@Override
	public AccountDto removeAccount(String email) {
		Account account = mongoTemplate.findAndRemove(new Query(Criteria.where("email").is(email)), Account.class);
		
		if (account == null) {
			log.error("Service: removeAccount: Account with email {} not found", email);
			throw new AccountNotFoundException();
		}
		log.debug("Service: removeAccount: Account with email {} has been removed", email);
		AccountDto accountRes = account.toDto();
		log.trace("Service: removeAccount: returns dto {}", accountRes);
		return accountRes;
	}

}
