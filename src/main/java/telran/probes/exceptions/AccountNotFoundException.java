package telran.probes.exceptions;

import static telran.probes.api.ServiceExceptionMessages.*;

@SuppressWarnings("serial")
public class AccountNotFoundException extends NotFoundException {
	public AccountNotFoundException() {
		super(ACCOUNT_NOT_FOUND);
	}

}
