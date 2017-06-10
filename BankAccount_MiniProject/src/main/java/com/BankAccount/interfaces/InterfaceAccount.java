package com.BankAccount.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.BankAccount.domainObjects.DomainAccount;

@Repository
public interface InterfaceAccount extends CrudRepository<DomainAccount, Long>{
    
    DomainAccount findOne(Long id);

}
