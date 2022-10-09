package com.bank.account.repository;

import com.bank.account.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByAccountIdOrderByOperationDateDesc(Long accountId);
    List<Operation> findByAccountId(Long accountId);
}
