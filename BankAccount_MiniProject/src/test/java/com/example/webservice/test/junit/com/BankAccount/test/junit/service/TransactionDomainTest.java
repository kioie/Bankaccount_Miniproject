package com.BankAccount.test.junit.service;

import com.BankAccount.domainObjects.SetDate;
import com.BankAccount.domainObjects.SetTransaction;
import com.BankAccount.domainObjects.TransactionDomain;
import com.BankAccount.interfaces.InterfaceTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionDomainTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired 
	InterfaceTransaction transactionsDomain;
	
	@Test
	public void testFindByDateBetweenAndType() {
		this.entityManager.persist(new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 3000, new Date()));
		List<TransactionDomain> transactions = transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()), SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId());
		assertThat(transactions.get(0)).isNotNull();
		assertThat(transactions.get(0).getType()).isEqualTo(SetTransaction.WITHDRAWAL.getId());
		assertThat(transactions.get(0).getAmount()).isEqualTo(3000);
		assertThat(transactions.get(0).getDate()).isBetween(SetDate.getStartOfDay(new Date()), SetDate.getEndOfDay(new Date()));
	}

}
