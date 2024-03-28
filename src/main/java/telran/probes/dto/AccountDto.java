package telran.probes.dto;

import jakarta.validation.constraints.*;
import static telran.probes.api.AccountingValidationErrorMessages.*;

public record AccountDto(@Email(message = WRONG_EMAIL_FORMAT) @NotBlank(message = EMPTY_EMAIL) String email, 
		@Size(min = PASSWORD_MIN_LENGTH, message = WRONG_PASSWORD_LENGTH) @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_WHITESPACES) String password, 
		@NotEmpty(message = EMPTY_ROLES) String[] roles) {

}
