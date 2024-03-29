package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.probes.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

}
