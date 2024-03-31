package telran.probes.service;


import static telran.probes.api.ServiceExceptionMessages.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
		log.debug("AccountingManagementServiceImpl: addAccount: Account created {} with encoded password", accountDoc);
		try {
			mongoTemplate.insert(accountDoc);
			log.debug("AccountingManagementServiceImpl: addAccount: Account {} succesfully saved to db", accountDoc);
		} catch (DuplicateKeyException e) {
			log.error("AccountingManagementServiceImpl: addAccount: Account with email {} already exists", accountDoc.getEmail());
			throw new AccountIllegalStateException();
		}
		AccountDto accountRes = accountDoc.toDto();
		log.trace("AccountingManagementServiceImpl: addAccount: returns dto {}", accountRes);
		return accountRes;
	}

	@Override
	public AccountDto removeAccount(String email) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!email.equals(username)) {
			log.error("AccountingManagementServiceImpl: removeAccount: {} attempts to remove {}", username, email);
			throw new AccessDeniedException(REMOVE_ANOTHER_ACCOUNT_PASSWORD);
		}
		Account account = mongoTemplate.findAndRemove(new Query(Criteria.where("email").is(email)), Account.class);
		
		if (account == null) {
			log.error("AccountingManagementServiceImpl: removeAccount: Account with email {} not found", email);
			throw new AccountNotFoundException();
		}
		log.debug("AccountingManagementServiceImpl: removeAccount: Account with email {} has been removed", email);
		AccountDto accountRes = account.toDto();
		log.trace("AccountingManagementServiceImpl: removeAccount: returns dto {}", accountRes);
		return accountRes;
	}

	@Override
	public void updatePassword(String email, String newPassword) throws AccountNotFoundException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!email.equals(username)) {
			log.error("AccountingManagementServiceImpl: updatePassword: {} attempts to update {} password", username, email);
			throw new AccessDeniedException(UPDATE_ANOTHER_ACCOUNT_PASSWORD);
		}
		Update update = new Update();
		update.set("hashPassword", passwordEncoder.encode(newPassword));
		Account account = mongoTemplate.findAndModify(new Query(Criteria.where("email").is(email)), update, Account.class);
		if (account == null) {
			log.error("AccountingManagementServiceImpl: removeAccount: Account with email {} not found", email);
			throw new AccountNotFoundException();
		}
		log.debug("AccountingManagementServiceImpl: updatePassword: Password of user {} has been updated", email);
	}

}
