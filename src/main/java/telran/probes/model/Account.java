package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
public class Account {
	@Id
	String email;
	String hashPassword;
	String[] roles;

}
