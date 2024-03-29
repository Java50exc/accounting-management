package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class BcryptTest {
	PasswordEncoder encoder;

	@Test
	void bcryptDefault() {
		encoder = new BCryptPasswordEncoder();
		String password = "user1234";
		String hashCode = encoder.encode(password);
		assertTrue(encoder.matches(password, hashCode));
		assertFalse(encoder.matches("kuku", password));
	}
	
	@Test
	void bcryptStrength() {
		encoder = new BCryptPasswordEncoder(16);
		String password = "user1234";
		String hashCode = encoder.encode(password);
		assertTrue(encoder.matches(password, hashCode));
		assertFalse(encoder.matches("kuku", password));
	}

}
