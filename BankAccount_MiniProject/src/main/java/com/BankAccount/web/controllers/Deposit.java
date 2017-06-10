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
@RequestMapping("/deposit")
public class Deposit extends Initializr {
    
    private static final double MAX_PER_TRANSACTION  = 40000; 
    private static final double MAX_PER_DAY = 150000; 
    private static final int MAX_FREQUENCY = 4;
    
    @RequestMapping(value="/", method = RequestMethod.POST)
    public @ResponseBody Responses makeDeposit(@RequestBody com.BankAccount.domainObjects.UserTransaction userTransaction) {
        
        Responses jsonResponse = new Responses();
        
        try {
            
            double total = 0;
            
            // check maximum limit deposit for the day has been reached
            List<TransactionDomain> deposits  = transactionsService.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                    SetDate.getEndOfDay(new Date()), SetTransaction.DEPOSIT.getId());
            
            if (deposits.size() > 0) {
                for (TransactionDomain accountTransaction: deposits) {
                    total+=accountTransaction.getAmount(); 
                }
                if (total  + userTransaction.getAmount()  > MAX_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Daily maximum deposit of $150,000 has been exceeded");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }
            
            // Check whether the amount being deposited exceeds the MAX_PER_TRANSACTION
            if(userTransaction.getAmount() > MAX_PER_TRANSACTION) {                
                jsonResponse.setSuccess(false, "Error", "You cannot deposit more than $40,000 per transaction");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }
            
            // check whether transactions exceeds the max allowed per day
            if (deposits.size() < MAX_FREQUENCY) {
                TransactionDomain accountTransaction = new TransactionDomain(SetTransaction.DEPOSIT.getId(), userTransaction.getAmount(), new Date());
                double amount  = transactionsService.save(accountTransaction).getAmount();
                
                DomainAccount account = accountService.findOne(ACCOUNT_ID);
                double newBalance = account.getAmount() + amount;
                account.setAmount(newBalance);
                accountService.save(account);
                
                jsonResponse.setSuccess(true, "", "Successfully deposited $"+account.getAmount());
                jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
                
            } else {
                jsonResponse.setSuccess(false, "Error", "Maximum daily transaction limit has been exceeded. Please try again tomorrow.");
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
