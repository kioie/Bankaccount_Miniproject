package com.BankAccount.test.junit.web.controllers;

import com.BankAccount.interfaces.InterfaceAccount;
import com.BankAccount.interfaces.InterfaceTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


public class InitializrTests {
	
	@Autowired
    protected MockMvc mvc;

    @MockBean
    protected InterfaceAccount domainAccount;
    
    @MockBean
    protected InterfaceTransaction transactionsDomain;

}
