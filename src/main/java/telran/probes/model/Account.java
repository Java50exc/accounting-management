package telran.probes.model;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import telran.probes.dto.AccountDto;

@Document(collection = "accounts")
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	@Id
	String email;
	String hashPassword;
	String[] roles;
	
	@Transient
	Integer passLength;
	
	public AccountDto toDto() {
		return new AccountDto(email, passToAsterisk(hashPassword), roles);
	}
	
	
	private String passToAsterisk(String hashPassword) {
		return "*".repeat(passLength == null ? hashPassword.length() : passLength);
	}

}
