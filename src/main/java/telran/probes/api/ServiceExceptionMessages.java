package telran.probes.api;

public interface ServiceExceptionMessages {
	String ACCOUNT_ALREADY_EXISTS = "Account with given email already exists";
	String ACCOUNT_NOT_FOUND = "Account with a given email not exists";
	
	String UPDATE_ANOTHER_ACCOUNT_PASSWORD = "Only current authenticated user may update its password";
	String REMOVE_ANOTHER_ACCOUNT_PASSWORD = "Only current authenticated user may remove his account";

}
