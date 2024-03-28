package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import telran.probes.dto.AccountDto;

@Document(collection = "${app.accounts.collection.name}")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	@Id
	String email;
	String hashPassword;
	String[] roles;
	
	public AccountDto toDto() {
		return new AccountDto(email, passToAsterisk(hashPassword), roles);
	}
	
	
	private String passToAsterisk(String hashPassword) {
		return "*".repeat(hashPassword.length());
	}

}
