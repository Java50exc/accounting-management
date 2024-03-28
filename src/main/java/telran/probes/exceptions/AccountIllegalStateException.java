package telran.probes.exceptions;

import static telran.probes.api.ServiceExceptionMessages.*;

@SuppressWarnings("serial")
public class AccountIllegalStateException extends IllegalStateException {
	public AccountIllegalStateException() {
		super(ACCOUNT_ALREADY_EXISTS);
	}

}
