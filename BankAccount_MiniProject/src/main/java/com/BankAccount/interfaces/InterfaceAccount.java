package com.BankAccount.interfaces;

import com.BankAccount.domainObjects.DomainAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterfaceAccount extends CrudRepository<DomainAccount, Long>{
    
    DomainAccount findOne(Long id);

}
