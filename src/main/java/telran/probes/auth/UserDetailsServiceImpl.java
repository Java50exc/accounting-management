package telran.probes.auth;

import java.util.Arrays;
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepo.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException(""));
		String[] roles = Arrays.stream(account.getRoles())
				.map(r -> "ROLE_" + r).toArray(String[]::new);

		return new User(username, account.getHashPassword(), 
				AuthorityUtils.createAuthorityList(roles));
	}

}
