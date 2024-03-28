package telran.probes.api;

public interface AccountingValidationErrorMessages {
	int PASSWORD_MIN_LENGTH = 8;
	String PASSWORD_REGEXP = "^\\S*$";
	String WRONG_EMAIL_FORMAT = "Wrong email format";
	String EMPTY_EMAIL_MSG = "Email must not be empty";
	String WRONG_PASSWORD_LENGTH = "Password length must at least " + PASSWORD_MIN_LENGTH + " characters";
	String PASSWORD_WHITESPACES = "Password cannot include whitespaces";
	String EMPTY_PASSWORD = "Password must not be empty";
	String EMPTY_ROLES = "Roles list must not be empty";

}
