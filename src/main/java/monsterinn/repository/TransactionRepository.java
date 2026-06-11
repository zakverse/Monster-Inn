package monsterinn.repository;

import monsterinn.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    // Lu nggak perlu nulis query manual, 
    // JpaRepository udah bawain findAll(), save(), delete(), dll.
}