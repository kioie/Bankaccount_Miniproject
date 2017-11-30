package com.BankAccount.test.junit.service;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.interfaces.InterfaceAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountDomainTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired 
	InterfaceAccount domainAccount;
	
	@Test
	public void testFindOne() {
		this.entityManager.persist(new DomainAccount(100.0));
		this.domainAccount.findAll(); 
		DomainAccount account = this.domainAccount.findOne(2L);
		assertThat(account.getAmount()).isEqualTo(100.0);
		
	}
	
}

