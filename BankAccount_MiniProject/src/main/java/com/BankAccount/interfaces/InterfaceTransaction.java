package com.BankAccount.interfaces;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.BankAccount.domainObjects.TransactionDomain;


@Repository
public interface InterfaceTransaction extends CrudRepository<TransactionDomain, Long>{
	
	List<TransactionDomain> findByDateBetweenAndType(Date StartOfDay, Date endOfDay, int type);

}
