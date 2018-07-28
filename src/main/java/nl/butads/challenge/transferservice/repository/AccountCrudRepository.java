package nl.butads.challenge.transferservice.repository;

import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCrudRepository extends JpaRepository<AccountDbo, String> {

}
