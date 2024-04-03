package telran.probes.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	@Value("${app.password.strength:10}")
	int strength;

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(strength);
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.cors(c -> c.disable()).csrf(c -> c.disable())
				.httpBasic(Customizer.withDefaults())
				.authorizeHttpRequests(c -> c
						.requestMatchers(HttpMethod.POST).hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE).hasRole("USER")
						.requestMatchers(HttpMethod.PUT).authenticated()
						.anyRequest().permitAll())
				.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.build();
	}

}
