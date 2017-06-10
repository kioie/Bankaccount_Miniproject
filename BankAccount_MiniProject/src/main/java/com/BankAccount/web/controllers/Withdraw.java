package com.BankAccount.web.controllers;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.domainObjects.SetDate;
import com.BankAccount.domainObjects.SetTransaction;
import com.BankAccount.domainObjects.TransactionDomain;
import com.BankAccount.responses.Responses;


@RestController
@RequestMapping(value = "/withdraw")
public class Withdraw extends Initializr {
	
    private static final int MAX_FREQUENCY = 3;
	private static final double MAX__PER_TRANSACTION  = 20000; 
    private static final double MAX_PER_DAY = 50000; 

	
	@RequestMapping(value="/", method = RequestMethod.POST)
    public @ResponseBody Responses makeWithDrawal(@RequestBody com.BankAccount.domainObjects.UserTransaction userTransaction) {
        
        Responses jsonResponse = new Responses();
        
        try {
            
            double total = 0;
            
            // check balance
            double balance = accountService.findOne(ACCOUNT_ID).getAmount();
            if (userTransaction.getAmount() > balance) {
            	jsonResponse.setSuccess(false, "Error", "You have insufficient funds");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            
            // check maximum limit withdrawal for the day has been reached
            List<TransactionDomain> withdrawals  = transactionsService.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                    SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId());
            
            if (withdrawals.size() > 0) {
                for (TransactionDomain accountTransaction: withdrawals) {
                    total+=accountTransaction.getAmount(); 
                }
                if (total + userTransaction.getAmount() > MAX_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Maximum daily withdrawal of $50,000");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }
            
            // Check whether the amount being withdrawn exceeds the MAX_PER_TRANSACTION
            if(userTransaction.getAmount() > MAX__PER_TRANSACTION) {                
                jsonResponse.setSuccess(false, "Error", "Maximum withdrawal limit exceeded");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            // check whether transactions exceeds the max allowed per day
            if (withdrawals.size() < MAX_FREQUENCY) {
                TransactionDomain accountTransaction = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), userTransaction.getAmount(), new Date());
                double amount  = transactionsService.save(accountTransaction).getAmount();
                
                DomainAccount account = accountService.findOne(ACCOUNT_ID);
                double newBalance = account.getAmount() - amount;
                account.setAmount(newBalance);
                accountService.save(account);
                
                jsonResponse.setSuccess(true, "", "Successfully withdrawn $"+amount);
                jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
                
            } else {
                jsonResponse.setSuccess(false, "Error", "Maximum daily withdrawal limit has been exceeded");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
            }
            
        } catch (Exception e) {
            logger.error("exception", e);
            jsonResponse.setSuccess(false, Responses.DEFAULT_MSG_TITLE_VALUE, Responses.DEFAULT_MSG_NAME_VALUE);
            jsonResponse.setHttpResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return jsonResponse;
        }
        
        return jsonResponse;
    }

}
