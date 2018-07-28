package nl.butads.challenge.transferservice.repository;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import nl.butads.challenge.transferservice.model.dbo.AccountDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

@Transactional(value = TxType.NOT_SUPPORTED)
public interface AccountReadonlyRepository extends JpaRepository<AccountDbo, String> {

}
