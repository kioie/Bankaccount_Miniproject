package com.BankAccount.test.junit.web.controllers;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.web.controllers.CheckBalance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CheckBalance.class)
public class CheckBalanceTest extends InitializrTests {
	
	@Autowired
    private MockMvc mvc;
    
    @Test
    public void testGetBalance() throws Exception {
        given(this.domainAccount.findOne(1L))
                .willReturn(new DomainAccount(100));
        this.mvc.perform(get("/checkbalance/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":true,\"messages\":{},\"errors\":{},\"data\":{\"balance\":\"$100.0\"},\"httpResponseCode\":200}"));
    }

}
