package com.BankAccount.test.junit.web.controllers;

import com.BankAccount.domainObjects.*;
import com.BankAccount.web.controllers.Withdraw;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(Withdraw.class)
public class WithdrawTests extends InitializrTests {
	
	@Test
    public void testWithdrawalExceedsCurrentBalance() throws Exception {
		
		UserTransaction userTransaction = new UserTransaction(50000);
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(40000));
        
        this.mvc.perform(post("/withdraw/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"You have insufficient funds\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
		
	}
	
	@Test
    public void testMaxWithdrawalForTheDay() throws Exception {
		
		TransactionDomain transaction = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 40000, new Date());
    	TransactionDomain transaction2 = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 5000, new Date());
    	
    	List<TransactionDomain> list = new ArrayList<>();
    	list.add(transaction);
    	list.add(transaction2);
    	
    	UserTransaction userTransaction = new UserTransaction(8000);
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(400000));
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId())).willReturn(list);
        
        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Withdrawal per day should not be more than $50K\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
		
	}
	
	@Test
    public void testMaxWithdrawalPerTransaction() throws Exception {
		
    	TransactionDomain transaction = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 5000, new Date());
    	TransactionDomain transaction2 = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 7500, new Date());
    	
    	List<TransactionDomain> list = new ArrayList<>();
    	list.add(transaction);
    	list.add(transaction2);
    	
    	UserTransaction userTransaction = new UserTransaction(25000);
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(400000));
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId())).willReturn(list);
        
        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Exceeded Maximum Withdrawal Per Transaction\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
		
	}
	
	@Test
    public void testMaxAllowedWithdrawalPerDay() throws Exception {
		
    	TransactionDomain transaction = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 5000, new Date());
    	TransactionDomain transaction2 = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 7500, new Date());
    	TransactionDomain transaction3 = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 10500, new Date());
    	
    	List<TransactionDomain> list = new ArrayList<>();
    	list.add(transaction);
    	list.add(transaction2);
    	list.add(transaction3);
    	
    	UserTransaction userTransaction = new UserTransaction(1000);
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(400000));
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId())).willReturn(list);
        
        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Maximum Withdrawal transactions for the day Exceeded\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
		
	}
	
	@Test
    public void testSuccessfulWithdrawal() throws Exception {
		
    	TransactionDomain transaction = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 5000, new Date());
    	TransactionDomain transaction2 = new TransactionDomain(SetTransaction.WITHDRAWAL.getId(), 7500, new Date());
    	
    	List<TransactionDomain> list = new ArrayList<>();
    	list.add(transaction);
    	list.add(transaction2);
    	
    	UserTransaction userTransaction = new UserTransaction(1000);
    	Gson gson = new Gson();
        String json = gson.toJson(userTransaction);
        
        given(this.domainAccount.findOne(1L)).willReturn(new DomainAccount(70000));  
        
        given(this.transactionsDomain.findByDateBetweenAndType(SetDate.getStartOfDay(new Date()),
                SetDate.getEndOfDay(new Date()), SetTransaction.WITHDRAWAL.getId())).willReturn(list);
        
        when(this.transactionsDomain.save(any(TransactionDomain.class))).thenReturn(transaction);
        when(this.domainAccount.save(any(DomainAccount.class))).thenReturn(new DomainAccount(400));
        
        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":true,\"messages\":{\"message\":\"Withdrawal sucessfully Transacted\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}"));
		
	}	

}
