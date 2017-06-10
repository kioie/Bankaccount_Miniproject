package com.BankAccount.domainObjects;


public enum SetTransaction {
    
    DEPOSIT(1), WITHDRAWAL(2);
    int id;
    
    private SetTransaction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    

}
