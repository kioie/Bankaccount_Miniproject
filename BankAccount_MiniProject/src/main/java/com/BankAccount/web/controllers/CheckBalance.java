package com.BankAccount.web.controllers;

import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.BankAccount.domainObjects.DomainAccount;
import com.BankAccount.responses.Responses;


@RestController
@RequestMapping(value="/checkbalance")
public class CheckBalance extends Initializr { 
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public @ResponseBody Responses getBalance() {
        
        Responses jsonResponse = new Responses();
        HashMap<String, Object> responseData = new HashMap<>();
        
        try {
            DomainAccount account = accountService.findOne(ACCOUNT_ID);
            
            if (account != null) {
                responseData.put("balance", "$"+ account.getAmount());
                
                jsonResponse.setSuccess(true);
                jsonResponse.setData(responseData);
                jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
            } else {
                jsonResponse.setSuccess(false, "Resource not found", Responses.RESOURCE_NOT_FOUND_MSG);
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NO_CONTENT);
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
