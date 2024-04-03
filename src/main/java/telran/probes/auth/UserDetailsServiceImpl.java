package telran.probes.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import telran.probes.model.Account;
import telran.probes.repo.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	final AccountRepository accountRepo;
	@Value("${app.root.password}")
	String rootPassword;
	@Value("${app.root.username:root@com.il}")
	String rootUsername;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		if (!username.equals(rootUsername)) {
			Account account = accountRepo.findById(username)
					.orElseThrow(() -> new UsernameNotFoundException(""));
			String[] roles = Arrays.stream(account.getRoles())
					.map(r -> "ROLE_" + r).toArray(String[]::new);
			user = new User(username, account.getHashPassword(), 
					AuthorityUtils.createAuthorityList(roles));
		} else {
			user = new User(rootUsername, rootPassword, AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));
		}
		return user;
	}
	
	

}
