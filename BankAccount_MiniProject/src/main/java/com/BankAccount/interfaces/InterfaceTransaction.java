package com.BankAccount.interfaces;

import com.BankAccount.domainObjects.TransactionDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface InterfaceTransaction extends CrudRepository<TransactionDomain, Long>{
	
	List<TransactionDomain> findByDateBetweenAndType(Date StartOfDay, Date endOfDay, int type);

}
