package telran.probes.controller;

import static telran.probes.api.AccountingValidationErrorMessages.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;
import telran.probes.service.AccountingManagementService;

@RestController
@RequestMapping("${app.accounts.api.path}")
@RequiredArgsConstructor
@Slf4j
public class AccountingManagementController {
	final AccountingManagementService accountService;
	
	@PostMapping
	AccountDto addAccount(@RequestBody @Valid AccountDto account) {
		log.debug("AccountingManagementController: addAccount: received {}", account);
		return accountService.addAccount(account);
	}
	
	@DeleteMapping("{email}")
	AccountDto removeAccount(@PathVariable("email") @Email(message = WRONG_EMAIL_FORMAT) @NotEmpty(message = EMPTY_EMAIL_MSG) String email) {
		log.debug("AccountingManagementController: removeAccount: received {}", email);
		return accountService.removeAccount(email);
	}
	
	@PutMapping
	PasswordUpdateData updatePassword(@RequestBody @Valid PasswordUpdateData accountUpdate) {
		log.debug("AccountingManagementController: addAccount: received {}", accountUpdate);
		accountService.updatePassword(accountUpdate.email(), accountUpdate.newPassword());
		return accountUpdate;
	}

}
