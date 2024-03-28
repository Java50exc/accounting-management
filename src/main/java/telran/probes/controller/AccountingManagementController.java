package telran.probes.controller;

import static telran.probes.api.AccountingValidationErrorMessages.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.AccountDto;
import telran.probes.service.AccountingManagementService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountingManagementController {
	final AccountingManagementService accountService;
	
	
	@PostMapping("${app.accounts.api.path}")
	AccountDto addAccount(@RequestBody @Valid AccountDto account) {
		log.debug("Controller: addAccount received {}", account);
		return accountService.addAccount(account);
	}
	
	@DeleteMapping("${app.accounts.api.path}" + "/{email}")
	AccountDto removeAccount(@PathVariable("email") @Email(message = WRONG_EMAIL_FORMAT) @NotBlank(message = EMPTY_EMAIL_MSG) String email) {
		log.debug("Controller: removeAccount received {}", email);
		return accountService.removeAccount(email);
	}

}
