package telran.probes.dto;

import jakarta.validation.constraints.*;
import static telran.probes.api.AccountingValidationErrorMessages.*;

import java.util.Arrays;
import java.util.Objects;

public record AccountDto(@Email(message = WRONG_EMAIL_FORMAT) @NotBlank(message = EMPTY_EMAIL_MSG) String email,
		@NotNull(message = EMPTY_PASSWORD) @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_WHITESPACES) @Size(min = PASSWORD_MIN_LENGTH, message = WRONG_PASSWORD_LENGTH) String password,
		@NotEmpty(message = EMPTY_ROLES) String[] roles) {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(roles);
		result = prime * result + Objects.hash(email, password);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountDto other = (AccountDto) obj;
		return Objects.equals(email, other.email) && Arrays.equals(roles, other.roles);
		
		
	}

	@Override
	public String toString() {
		return "AccountDto [email=" + email + ", password=" + password + ", roles=" + Arrays.toString(roles) + "]";
	}

}
