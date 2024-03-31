package telran.probes.dto;

import static telran.probes.api.AccountingValidationErrorMessages.*;

import java.util.Objects;

import jakarta.validation.constraints.*;

public record PasswordUpdateData(
		@Email(message = WRONG_EMAIL_FORMAT) @NotBlank(message = EMPTY_EMAIL_MSG) String email,
		@NotNull(message = EMPTY_PASSWORD) @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_WHITESPACES) @Size(min = PASSWORD_MIN_LENGTH, message = WRONG_PASSWORD_LENGTH) String newPassword) {

	@Override
	public int hashCode() {
		return Objects.hash(email, newPassword);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordUpdateData other = (PasswordUpdateData) obj;
		return Objects.equals(email, other.email) && Objects.equals(newPassword, other.newPassword);
	}

	@Override
	public String toString() {
		return "PasswordUpdateData [email=" + email + ", newPassword=" + newPassword + "]";
	}
	
	

}
