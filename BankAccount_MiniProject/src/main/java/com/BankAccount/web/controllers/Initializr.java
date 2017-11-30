package com.BankAccount.web.controllers;

import com.BankAccount.interfaces.InterfaceAccount;
import com.BankAccount.interfaces.InterfaceTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Initializr {
	
	protected final Log logger = LogFactory.getLog(getClass());
    
    protected static final long ACCOUNT_ID = 1L;
    
    @Autowired
    InterfaceAccount accountService;
    
    @Autowired
    InterfaceTransaction transactionsService; 

}
