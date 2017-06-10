package com.BankAccount.test.junit.web.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.domainObjects.SetDate;
import com.BankAccount.domainObjects.SetTransaction;
import com.BankAccount.domainObjects.TransactionDomain;
import com.BankAccount.domainObjects.UserTransaction;
import com.BankAccount.web.controllers.Deposit;
import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@WebMvcTest(Deposit.class)
public class DepositTests extends InitializrTests {
    
    @Test
    public void testMaxDepositForTheDay() throws Exception {
    	TransactionDomain transaction = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 100000, new Date());
    	TransactionDomain transaction2 = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 40000, new Date());
    	
    	List<TransactionDomain> list = new ArrayList<>();
    	list.add(transaction);
    	list.add(transaction2);
    	
    	UserTransaction userTransaction = new UserTransaction(15000); 
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
    	
    	given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.DEPOSIT.getId())).willReturn(list);
        this.mvc.perform(post("/deposit/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Deposit for the day should not be more than $150K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
    }
    
    @Test
    public void testMaxDepositPerTransaction() throws Exception {
        TransactionDomain transaction = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 50000, new Date());
        TransactionDomain transaction2 = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 50000, new Date());
        
        List<TransactionDomain> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);
        
        UserTransaction userTransaction = new UserTransaction(50000); // deposit of $50K
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.DEPOSIT.getId())).willReturn(list);
        this.mvc.perform(post("/deposit/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Deposit per transaction should not be more than $40K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
    }
    
    @Test
    public void testMaxAllowedDepositPerDay() throws Exception {
        TransactionDomain transaction = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 100000, new Date());
        TransactionDomain transaction2 = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 10000, new Date());
        TransactionDomain transaction3 = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 5000, new Date());
        TransactionDomain transaction4 = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 200, new Date());
        
        List<TransactionDomain> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);
        list.add(transaction3);
        list.add(transaction4);
        
        UserTransaction userTransaction = new UserTransaction(5000); // $5K deposit
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.DEPOSIT.getId())).willReturn(list);
        this.mvc.perform(post("/deposit/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"maximum transactions for the day Exceeded\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
    }
    
    @Test
    public void testSuccessfulDeposit() throws Exception {
        TransactionDomain transaction = new TransactionDomain(SetTransaction.DEPOSIT.getId(), 50000, new Date());
        
        List<TransactionDomain> list = new ArrayList<>();
        list.add(transaction);
        
        UserTransaction userTransaction = new UserTransaction(5000); // $5K deposit
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.DEPOSIT.getId())).willReturn(list);
        given(this.transactionsDomain.save(transaction)).willReturn(transaction);
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(50000));
        
        when(this.transactionsDomain.save(any(TransactionDomain.class))).thenReturn(transaction);
        when(this.domainAccount.save(any(DomainAccount.class))).thenReturn(new DomainAccount(50000));
        
        this.mvc.perform(post("/deposit/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":true,\"messages\":{\"message\":\"Deposit sucessfully Transacted\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}"));
    }

}
