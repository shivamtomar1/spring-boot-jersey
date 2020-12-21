package com.microservice.nav.dao;

/**
 * 
 * Provide DAO for response from https://raw.githubusercontent.com/arcjsonapi/HoldingValueCalculator/master/api/holding
 * @author Shivam Tomar
 */
public class Holding {
//    {"date" : "20190101", "security" : "ABC Corporation", "quantity" : 980, "portfolio" : "Growth"}
    
    private String date;
    private String security;
    private int quantity;
    private String portfolio;
    
    public Holding(String date, String security, int quantity, String portfolio) {
        this.date = date;
        this.security = security;
        this.quantity = quantity;
        this.portfolio = portfolio;
    }

    public String getDate() {
        return date;
    }

    public String getSecurity() {
        return security;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPortfolio() {
        return portfolio;
    }
    
}
