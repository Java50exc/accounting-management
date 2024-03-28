package telran.probes.service;

import telran.probes.dto.AccountDto;

public interface AccountingManagementService {
	AccountDto addAccount(AccountDto account);
	AccountDto removeAccount(String email);

}
